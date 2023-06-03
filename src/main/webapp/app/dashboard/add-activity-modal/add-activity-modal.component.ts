import { Component, Inject, Input, LOCALE_ID, OnInit } from '@angular/core';
import { ModalDismissReasons, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { AbstractControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { DashboardService } from '../dashboard.service';
import { debounceTime, distinctUntilChanged, Observable, OperatorFunction } from 'rxjs';
import { map } from 'rxjs/operators';
import { ActivityService } from '../../entities/activity/service/activity.service';
import { IActivity } from '../../entities/activity/activity.model';

@Component({
  selector: 'jhi-add-activity-modal',
  templateUrl: './add-activity-modal.component.html',
  styleUrls: ['./add-activity-modal.component.scss'],
})
export class AddActivityModalComponent implements OnInit {
  @Input() env: any;
  @Input() tags: any;
  @Input() account: any;
  closeResult = '';
  addActivityForm: any;
  currentDate: string;
  loading = false;
  searching = false;
  searchFailed = false;
  tagObj: object[];

  control(controlName: string): AbstractControl {
    return this.addActivityForm.get(controlName);
  }

  constructor(private modalService: NgbModal, private fb: FormBuilder, @Inject(LOCALE_ID) private locale: string) {
    this.currentDate = this.formatDate(new Date());
  }

  ngOnInit(): void {
    this.addActivityForm = this.fb.group({
      name: [{ value: '' }, Validators.required],
      startedOn: [this.currentDate, Validators.required],
      finishedOn: [''],
      description: ['', Validators.required],
      link: ['', null],
      flagged: [false, null],
      tag: [[], null],
      environment: [this.env.id],
      owner: [this.account.id],
    });
  }

  private getDismissReason(reason: any): string {
    if (reason === ModalDismissReasons.ESC) {
      return 'by pressing ESC';
    } else if (reason === ModalDismissReasons.BACKDROP_CLICK) {
      return 'by clicking on a backdrop';
    } else {
      return `with: ${reason}`;
    }
  }

  open(content) {
    this.modalService.open(content, { ariaLabelledBy: 'modal-basic-title' }).result.then(
      result => {
        console.log(this.addActivityForm.value);
        this.closeResult = `Closed with: ${result}`;
      },
      reason => {
        this.closeResult = `Dismissed ${this.getDismissReason(reason)}`;
      }
    );
  }

  private formatDate(date: Date): string {
    const isoTime = new Date(date);
    const lengthOfIso8601SecondsAndMillis = ':ss.sssZ'.length;
    // desired format is YYYY-MM-DDTHH:mm, toISOString has the best match.
    return isoTime.toISOString().slice(0, -lengthOfIso8601SecondsAndMillis);
  }

  search: OperatorFunction<string, readonly string[]> = (text$: Observable<string>) =>
    text$.pipe(
      debounceTime(200),
      distinctUntilChanged(),
      map((term: string): string[] =>
        term.length < 2 ? [] : this.tags.filter(v => v.toLowerCase().indexOf(term.toLowerCase()) > -1).slice(0, 10)
      )
    );
}
