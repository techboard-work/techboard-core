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

describe('ActivityKind e2e test', () => {
  const activityKindPageUrl = '/activity-kind';
  const activityKindPageUrlPattern = new RegExp('/activity-kind(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const activityKindSample = { name: 'teal', description: 'Wooden Human', color: 'yellow' };

  let activityKind;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/activity-kinds+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/activity-kinds').as('postEntityRequest');
    cy.intercept('DELETE', '/api/activity-kinds/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (activityKind) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/activity-kinds/${activityKind.id}`,
      }).then(() => {
        activityKind = undefined;
      });
    }
  });

  it('ActivityKinds menu should load ActivityKinds page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('activity-kind');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ActivityKind').should('exist');
    cy.url().should('match', activityKindPageUrlPattern);
  });

  describe('ActivityKind page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(activityKindPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ActivityKind page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/activity-kind/new$'));
        cy.getEntityCreateUpdateHeading('ActivityKind');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', activityKindPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/activity-kinds',
          body: activityKindSample,
        }).then(({ body }) => {
          activityKind = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/activity-kinds+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [activityKind],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(activityKindPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ActivityKind page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('activityKind');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', activityKindPageUrlPattern);
      });

      it('edit button click should load edit ActivityKind page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ActivityKind');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', activityKindPageUrlPattern);
      });

      it('edit button click should load edit ActivityKind page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ActivityKind');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', activityKindPageUrlPattern);
      });

      it('last delete button click should delete instance of ActivityKind', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('activityKind').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', activityKindPageUrlPattern);

        activityKind = undefined;
      });
    });
  });

  describe('new ActivityKind page', () => {
    beforeEach(() => {
      cy.visit(`${activityKindPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ActivityKind');
    });

    it('should create an instance of ActivityKind', () => {
      cy.get(`[data-cy="name"]`).type('Organized').should('have.value', 'Organized');

      cy.get(`[data-cy="description"]`).type('Namibia').should('have.value', 'Namibia');

      cy.get(`[data-cy="color"]`).type('salmon').should('have.value', 'salmon');

      cy.get(`[data-cy="icon"]`).type('Indonesia Togo').should('have.value', 'Indonesia Togo');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        activityKind = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', activityKindPageUrlPattern);
    });
  });
});
