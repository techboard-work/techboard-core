import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'jhi-activities',
  templateUrl: './activities.component.html',
  styleUrls: ['./activities.component.scss'],
})
export class ActivitiesComponent implements OnInit {
  @Input() activities: any[];
  constructor() {}
  ngOnInit(): void {}
}
