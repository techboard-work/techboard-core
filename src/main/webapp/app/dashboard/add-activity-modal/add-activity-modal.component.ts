import { Component, Input, OnInit } from '@angular/core';
import { ModalDismissReasons, NgbDatepickerModule, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { AbstractControl, FormBuilder, Validators } from '@angular/forms';

@Component({
  selector: 'jhi-add-activity-modal',
  templateUrl: './add-activity-modal.component.html',
  styleUrls: ['./add-activity-modal.component.scss'],
})
export class AddActivityModalComponent implements OnInit {
  @Input() envName: string;
  closeResult = '';
  loading = false;
  addActivityForm;
  currentDate = new Date();

  control(controlName: string): AbstractControl {
    return this.addActivityForm.get(controlName);
  }

  constructor(private modalService: NgbModal, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.addActivityForm = this.fb.group({
      name: [{ value: 'ze naam', disabled: this.loading }, Validators.required],
      startedOn: [{ value: '' }, Validators.required],
      finishedOn: [''],
      description: ['le description....', Validators.required],
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

  saveActivity(): void {
    console.log(this.addActivityForm.value);
  }
}
