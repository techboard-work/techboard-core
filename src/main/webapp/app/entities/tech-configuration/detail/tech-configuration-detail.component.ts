import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITechConfiguration } from '../tech-configuration.model';

@Component({
  selector: 'jhi-tech-configuration-detail',
  templateUrl: './tech-configuration-detail.component.html',
})
export class TechConfigurationDetailComponent implements OnInit {
  techConfiguration: ITechConfiguration | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ techConfiguration }) => {
      this.techConfiguration = techConfiguration;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
