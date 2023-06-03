import { Component, OnInit } from '@angular/core';
import { IEnvironment } from '../entities/environment/environment.model';
import { DashboardService } from './dashboard.service';
import { Observable } from 'rxjs';
import { AccountService } from '../core/auth/account.service';

@Component({
  selector: 'jhi-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss'],
})
export class DashboardComponent implements OnInit {
  environments$: Observable<any>;
  environments: IEnvironment[];
  tags: any;
  account: any;
  closeResult: any = '';

  constructor(private dashboardSvc: DashboardService, private accountService: AccountService) {}

  ngOnInit(): void {
    this.environments$ = this.dashboardSvc.getEnvironments();
    this.accountService.identity().subscribe(account => (this.account = account));
    this.dashboardSvc.getTags().subscribe(tags => (this.tags = tags));
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
