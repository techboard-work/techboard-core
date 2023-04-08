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

describe('TechConfiguration e2e test', () => {
  const techConfigurationPageUrl = '/tech-configuration';
  const techConfigurationPageUrlPattern = new RegExp('/tech-configuration(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const techConfigurationSample = { version: 84896, timestamp: '2023-04-11T13:15:52.654Z', content: 'Sleek' };

  let techConfiguration;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/tech-configurations+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/tech-configurations').as('postEntityRequest');
    cy.intercept('DELETE', '/api/tech-configurations/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (techConfiguration) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/tech-configurations/${techConfiguration.id}`,
      }).then(() => {
        techConfiguration = undefined;
      });
    }
  });

  it('TechConfigurations menu should load TechConfigurations page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('tech-configuration');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('TechConfiguration').should('exist');
    cy.url().should('match', techConfigurationPageUrlPattern);
  });

  describe('TechConfiguration page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(techConfigurationPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create TechConfiguration page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/tech-configuration/new$'));
        cy.getEntityCreateUpdateHeading('TechConfiguration');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', techConfigurationPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/tech-configurations',
          body: techConfigurationSample,
        }).then(({ body }) => {
          techConfiguration = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/tech-configurations+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [techConfiguration],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(techConfigurationPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details TechConfiguration page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('techConfiguration');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', techConfigurationPageUrlPattern);
      });

      it('edit button click should load edit TechConfiguration page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TechConfiguration');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', techConfigurationPageUrlPattern);
      });

      it('edit button click should load edit TechConfiguration page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TechConfiguration');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', techConfigurationPageUrlPattern);
      });

      it('last delete button click should delete instance of TechConfiguration', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('techConfiguration').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', techConfigurationPageUrlPattern);

        techConfiguration = undefined;
      });
    });
  });

  describe('new TechConfiguration page', () => {
    beforeEach(() => {
      cy.visit(`${techConfigurationPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('TechConfiguration');
    });

    it('should create an instance of TechConfiguration', () => {
      cy.get(`[data-cy="version"]`).type('711286').should('have.value', '711286');

      cy.get(`[data-cy="timestamp"]`).type('2023-04-10T13:44').blur().should('have.value', '2023-04-10T13:44');

      cy.get(`[data-cy="content"]`).type('redundant Metal Orchard').should('have.value', 'redundant Metal Orchard');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        techConfiguration = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', techConfigurationPageUrlPattern);
    });
  });
});
