import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IActivityKind } from '../activity-kind.model';

@Component({
  selector: 'jhi-activity-kind-detail',
  templateUrl: './activity-kind-detail.component.html',
})
export class ActivityKindDetailComponent implements OnInit {
  activityKind: IActivityKind | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ activityKind }) => {
      this.activityKind = activityKind;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
