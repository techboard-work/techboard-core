import { Component, Inject, Input, LOCALE_ID, OnInit } from '@angular/core';
import { ModalDismissReasons, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { AbstractControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { DashboardService } from '../dashboard.service';
import { debounceTime, distinctUntilChanged, Observable, OperatorFunction } from 'rxjs';
import { map } from 'rxjs/operators';

@Component({
  selector: 'jhi-add-activity-modal',
  templateUrl: './add-activity-modal.component.html',
  styleUrls: ['./add-activity-modal.component.scss'],
})
export class AddActivityModalComponent implements OnInit {
  @Input() envName: string;
  closeResult = '';
  addActivityForm: any;
  currentDate: string;
  loading = false;
  searching = false;
  searchFailed = false;
  tagObj: object[];
  tags: string[];

  control(controlName: string): AbstractControl {
    return this.addActivityForm.get(controlName);
  }

  constructor(
    private modalService: NgbModal,
    private fb: FormBuilder,
    @Inject(LOCALE_ID) private locale: string,
    private dashboardSvc: DashboardService
  ) {
    this.currentDate = this.formatDate(new Date());
  }

  ngOnInit(): void {
    this.dashboardSvc.getTags().subscribe({
      next: (tags): void => {
        this.tagObj = tags;
        this.tags = tags.map(tagObj => tagObj.tag);
      },
      error: (err: any): void => {
        console.error(err);
      },
      complete: (): void => console.log('complete gettig tags'),
    });
    this.addActivityForm = this.fb.group({
      name: [{ value: 'The name of the activity', disabled: this.loading }, Validators.required],
      startedOn: [this.currentDate, Validators.required],
      finishedOn: [''],
      description: ['de description....', Validators.required],
      link: ['', null],
      flagged: [false, null],
      tag: ['', null],
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
