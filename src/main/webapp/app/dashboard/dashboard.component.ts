import { Component, OnInit } from '@angular/core';
import { IEnvironment } from '../entities/environment/environment.model';
import { DashboardService } from './dashboard.service';
import { Observable } from 'rxjs';

@Component({
  selector: 'jhi-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss'],
})
export class DashboardComponent implements OnInit {
  environments$: Observable<any>;
  environments: IEnvironment[];

  isOpen = true;
  constructor(private dashboardSvc: DashboardService) {}

  ngOnInit(): void {
    this.environments$ = this.dashboardSvc.getEnvironments();
  }
}
