import { Component, Inject, Input, LOCALE_ID, OnInit } from '@angular/core';
import { ModalDismissReasons, NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { AbstractControl, FormBuilder } from '@angular/forms';
import { ActivityService } from '../../entities/activity/service/activity.service';
import { ActivityFormService } from '../../entities/activity/update/activity-form.service';
import { NewActivity } from '../../entities/activity/activity.model';
import dayjs from 'dayjs/esm';

@Component({
  selector: 'jhi-add-activity-modal',
  templateUrl: './add-activity-modal.component.html',
  styleUrls: ['./add-activity-modal.component.scss'],
})
export class AddActivityModalComponent implements OnInit {
  @Input() env: any;
  @Input() allTags: any;
  @Input() account: any;
  closeResult = '';
  addActivityForm: any;
  currentDate: string;
  loading = false;
  searching = false;
  searchFailed = false;

  control(controlName: string): AbstractControl {
    return this.addActivityForm.get(controlName);
  }

  constructor(
    private modalService: NgbModal,
    private fb: FormBuilder,
    @Inject(LOCALE_ID)
    private locale: string,
    private activitySvc: ActivityService,
    private activityFormSvc: ActivityFormService
  ) {}

  ngOnInit(): void {
    this.addActivityForm = this.activityFormSvc.createActivityFormGroup();
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
    this.modalService.open(content).result.then(
      result => {
        this.saveActivity();
        this.closeResult = `Closed with: ${result}`;
      },
      reason => {
        this.closeResult = `Dismissed ${this.getDismissReason(reason)}`;
      }
    );
  }

  enrichNewActivity(activity): NewActivity {
    return {
      ...activity,
      startedOn: activity.startedOn ? dayjs(activity.startedOn) : undefined,
      finishedOn: activity.finishedOn ? dayjs(activity.finishedOn) : undefined,
      owner: this.account,
      environment: this.env,
    };
  }

  saveActivity(): void {
    let newActivity = this.enrichNewActivity(this.addActivityForm.value);
    this.activitySvc.create(newActivity).subscribe({
      next: resp => {
        console.log(resp);
      },
      error: error => console.log(error),
    });
  }
}
