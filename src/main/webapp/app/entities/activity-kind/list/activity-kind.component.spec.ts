import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ActivityKindService } from '../service/activity-kind.service';

import { ActivityKindComponent } from './activity-kind.component';

describe('ActivityKind Management Component', () => {
  let comp: ActivityKindComponent;
  let fixture: ComponentFixture<ActivityKindComponent>;
  let service: ActivityKindService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'activity-kind', component: ActivityKindComponent }]), HttpClientTestingModule],
      declarations: [ActivityKindComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: 'id,asc',
            }),
            queryParamMap: of(
              jest.requireActual('@angular/router').convertToParamMap({
                page: '1',
                size: '1',
                sort: 'id,desc',
              })
            ),
            snapshot: { queryParams: {} },
          },
        },
      ],
    })
      .overrideTemplate(ActivityKindComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ActivityKindComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ActivityKindService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.activityKinds?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to activityKindService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getActivityKindIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getActivityKindIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
