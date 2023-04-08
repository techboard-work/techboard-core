import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { TechConfigurationService } from '../service/tech-configuration.service';

import { TechConfigurationComponent } from './tech-configuration.component';

describe('TechConfiguration Management Component', () => {
  let comp: TechConfigurationComponent;
  let fixture: ComponentFixture<TechConfigurationComponent>;
  let service: TechConfigurationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'tech-configuration', component: TechConfigurationComponent }]),
        HttpClientTestingModule,
      ],
      declarations: [TechConfigurationComponent],
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
      .overrideTemplate(TechConfigurationComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TechConfigurationComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(TechConfigurationService);

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
    expect(comp.techConfigurations?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to techConfigurationService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getTechConfigurationIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getTechConfigurationIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
