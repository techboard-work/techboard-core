import { Component, OnInit } from '@angular/core';
import { IEnvironment } from '../entities/environment/environment.model';
import { DashboardService } from './dashboard.service';
import { Observable } from 'rxjs';
import { ModalDismissReasons, NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'jhi-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss'],
})
export class DashboardComponent implements OnInit {
  environments$: Observable<any>;
  environments: IEnvironment[];

  closeResult: any = '';

  isOpen = true;
  constructor(private dashboardSvc: DashboardService, private modalService: NgbModal) {}

  ngOnInit(): void {
    this.environments$ = this.dashboardSvc.getEnvironments();
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
        this.closeResult = `Closed with: ${result}`;
      },
      reason => {
        this.closeResult = `Dismissed ${this.getDismissReason(reason)}`;
      }
    );
  }
}
