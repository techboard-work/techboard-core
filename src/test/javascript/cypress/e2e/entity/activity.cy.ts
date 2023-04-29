import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('Activity e2e test', () => {
  const activityPageUrl = '/activity';
  const activityPageUrlPattern = new RegExp('/activity(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  // const activitySample = {"name":"grey Implementation","startedOn":"2023-05-01T12:34:09.042Z","flagged":false};

  let activity;
  // let environment;

  beforeEach(() => {
    cy.login(username, password);
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/environments',
      body: {"name":"Handmade Re-engineered","code":"calculate","color":"silver","level":76162,"link":"SSL Credit RAM"},
    }).then(({ body }) => {
      environment = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/activities+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/activities').as('postEntityRequest');
    cy.intercept('DELETE', '/api/activities/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/tags', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/environments', {
      statusCode: 200,
      body: [environment],
    });

    cy.intercept('GET', '/api/users', {
      statusCode: 200,
      body: [],
    });

  });
   */

  afterEach(() => {
    if (activity) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/activities/${activity.id}`,
      }).then(() => {
        activity = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (environment) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/environments/${environment.id}`,
      }).then(() => {
        environment = undefined;
      });
    }
  });
   */

  it('Activities menu should load Activities page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('activity');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Activity').should('exist');
    cy.url().should('match', activityPageUrlPattern);
  });

  describe('Activity page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(activityPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Activity page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/activity/new$'));
        cy.getEntityCreateUpdateHeading('Activity');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', activityPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/activities',
          body: {
            ...activitySample,
            environment: environment,
          },
        }).then(({ body }) => {
          activity = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/activities+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [activity],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(activityPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(activityPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details Activity page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('activity');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', activityPageUrlPattern);
      });

      it('edit button click should load edit Activity page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Activity');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', activityPageUrlPattern);
      });

      it('edit button click should load edit Activity page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Activity');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', activityPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of Activity', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('activity').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', activityPageUrlPattern);

        activity = undefined;
      });
    });
  });

  describe('new Activity page', () => {
    beforeEach(() => {
      cy.visit(`${activityPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Activity');
    });

    it.skip('should create an instance of Activity', () => {
      cy.get(`[data-cy="name"]`).type('generating Legacy Pound').should('have.value', 'generating Legacy Pound');

      cy.get(`[data-cy="startedOn"]`).type('2023-05-02T05:10').blur().should('have.value', '2023-05-02T05:10');

      cy.get(`[data-cy="finishedOn"]`).type('2023-05-02T02:14').blur().should('have.value', '2023-05-02T02:14');

      cy.get(`[data-cy="description"]`).type('killer Factors Heights').should('have.value', 'killer Factors Heights');

      cy.get(`[data-cy="link"]`).type('card Soft').should('have.value', 'card Soft');

      cy.get(`[data-cy="flagged"]`).should('not.be.checked');
      cy.get(`[data-cy="flagged"]`).click().should('be.checked');

      cy.get(`[data-cy="environment"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        activity = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', activityPageUrlPattern);
    });
  });
});
