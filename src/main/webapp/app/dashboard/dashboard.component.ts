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

  constructor(private dashboardSvc: DashboardService) {}

  ngOnInit(): void {
    this.environments$ = this.dashboardSvc.getEnvironments();
  }

  getEnvStatus(activities) {
    if (!activities) {
      return { class: 'bg-success', desc: 'This environment is up ad running' };
    } else if (activities.find(activity => activity.flagged === true)) {
      return { class: 'bg-danger', desc: 'This environment is flagged as not usable' };
    } else {
      return { class: 'bg-warning', desc: 'This environment should be used with caution' };
    }
  }
}
