import { Component, OnInit } from '@angular/core';
import { IEnvironment } from '../entities/environment/environment.model';
import { DashboardService } from './dashboard.service';
import { Observable } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

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

  getEnvStateClass(activities) {
    if (!activities) {
      return 'bg-success';
    }

    if (activities.find(activity => activity.flagged === true)) {
      return 'bg-danger';
    } else {
      return 'bg-warning';
    }
  }
}
