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

describe('Environment e2e test', () => {
  const environmentPageUrl = '/environment';
  const environmentPageUrlPattern = new RegExp('/environment(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const environmentSample = { name: 'GB open-source', label: 'Dollar', description: 'CSS', color: 'plum', level: 75223 };

  let environment;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/environments+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/environments').as('postEntityRequest');
    cy.intercept('DELETE', '/api/environments/*').as('deleteEntityRequest');
  });

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

  it('Environments menu should load Environments page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('environment');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Environment').should('exist');
    cy.url().should('match', environmentPageUrlPattern);
  });

  describe('Environment page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(environmentPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Environment page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/environment/new$'));
        cy.getEntityCreateUpdateHeading('Environment');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', environmentPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/environments',
          body: environmentSample,
        }).then(({ body }) => {
          environment = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/environments+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [environment],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(environmentPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Environment page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('environment');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', environmentPageUrlPattern);
      });

      it('edit button click should load edit Environment page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Environment');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', environmentPageUrlPattern);
      });

      it('edit button click should load edit Environment page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Environment');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', environmentPageUrlPattern);
      });

      it('last delete button click should delete instance of Environment', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('environment').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', environmentPageUrlPattern);

        environment = undefined;
      });
    });
  });

  describe('new Environment page', () => {
    beforeEach(() => {
      cy.visit(`${environmentPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Environment');
    });

    it('should create an instance of Environment', () => {
      cy.get(`[data-cy="name"]`).type('Peso Salad Cotton').should('have.value', 'Peso Salad Cotton');

      cy.get(`[data-cy="label"]`).type('e-services').should('have.value', 'e-services');

      cy.get(`[data-cy="description"]`).type('Pants').should('have.value', 'Pants');

      cy.get(`[data-cy="color"]`).type('olive').should('have.value', 'olive');

      cy.get(`[data-cy="level"]`).type('47360').should('have.value', '47360');

      cy.get(`[data-cy="link"]`).type('Refined Branding virtual').should('have.value', 'Refined Branding virtual');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        environment = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', environmentPageUrlPattern);
    });
  });
});
