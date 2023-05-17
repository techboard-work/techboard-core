import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { DashboardService } from '../dashboard.service';
import { Observable, switchMap } from 'rxjs';

@Component({
  templateUrl: './environment.component.html',
  styleUrls: ['./environment.component.scss'],
})
export class EnvironmentComponent implements OnInit {
  public environment$: Observable<any>;

  constructor(private route: ActivatedRoute, private dashboardSvc: DashboardService) {}

  ngOnInit(): void {
    this.environment$ = this.route.paramMap.pipe(switchMap((params: ParamMap) => this.dashboardSvc.getEnvironment(params.get('envId')!)));
  }
}
