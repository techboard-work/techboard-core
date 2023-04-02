import { Component, OnInit } from '@angular/core';
import { IEnvironment } from '../entities/environment/environment.model';
import { EnvironmentService } from '../entities/environment/service/environment.service';
import { Observable } from 'rxjs';

@Component({
  selector: 'jhi-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss'],
})
export class DashboardComponent implements OnInit {
  environmets$: Observable<any>;
  environments: IEnvironment[];

  constructor(private environmentService: EnvironmentService) {}

  isOpen = true;

  ngOnInit(): void {
    this.environmets$ = this.environmentService.getEnvironments();
  }
}
