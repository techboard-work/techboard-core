import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ActivityKindDetailComponent } from './activity-kind-detail.component';

describe('ActivityKind Management Detail Component', () => {
  let comp: ActivityKindDetailComponent;
  let fixture: ComponentFixture<ActivityKindDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ActivityKindDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ activityKind: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ActivityKindDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ActivityKindDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load activityKind on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.activityKind).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
