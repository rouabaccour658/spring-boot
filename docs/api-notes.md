# Notes sur l'API AutoLoc

## A. Tableau des endpoints

Les codes d'erreur indiqués sont ceux documentés par les contrôleurs ou produits par `ApiExceptionHandler` : `400` pour une donnée invalide et `404` pour une ressource absente. Les erreurs techniques non prévues par l'application ne sont pas détaillées ici.

| Contrôleur | Méthode | Chemin | Service utilisé | Succès | Erreurs possibles |
|---|---|---|---|---|---|
| ClientController | GET | `/api/clients` | `IClientService.findAll()` | 200 | — |
| ClientController | GET | `/api/clients/{id}` | `IClientService.findById(id)` | 200 | 404 |
| ClientController | POST | `/api/clients` | `IClientService.create(client)` | 201 | 400 (nom/prénom invalides) |
| ClientController | PUT | `/api/clients/{id}` | `IClientService.update(id, client)` | 200 | 400 (données invalides) ; 404 |
| ClientController | DELETE | `/api/clients/{id}` | `IClientService.deleteById(id)` | 204 | 404 |
| AgenceController | GET | `/api/agences` | `IAgenceService.findAll()` | 200 | — |
| AgenceController | GET | `/api/agences/{id}` | `IAgenceService.findById(id)` | 200 | 404 |
| AgenceController | POST | `/api/agences` | `IAgenceService.create(agence)` | 201 | 400 (agence invalide) |
| AgenceController | PUT | `/api/agences/{id}` | `IAgenceService.update(id, agence)` | 200 | 400 (données/identifiant invalides) ; 404 |
| AgenceController | DELETE | `/api/agences/{id}` | `IAgenceService.deleteById(id)` | 204 | 404 |
| ContratController | GET | `/api/contrats` | `IContratService.findAll()` | 200 | — |
| ContratController | GET | `/api/contrats/{id}` | `IContratService.findById(id)` | 200 | 404 |
| ContratController | POST | `/api/contrats` | `IContratService.create(contrat)` | 201 | 400 (données invalides) |
| ContratController | PUT | `/api/contrats/{id}` | `IContratService.update(id, contrat)` | 200 | 400 (données/identifiant invalides) ; 404 |
| ContratController | DELETE | `/api/contrats/{id}` | `IContratService.deleteById(id)` | 204 | 404 |
| EmployeController | GET | `/api/employes` | `IEmployeService.findAll()` | 200 | — |
| EmployeController | GET | `/api/employes/{id}` | `IEmployeService.findById(id)` | 200 | 404 |
| EmployeController | POST | `/api/employes` | `IEmployeService.create(request)` | 201 | 400 (données invalides) ; 404 (agence absente) |
| EmployeController | PUT | `/api/employes/{id}` | `IEmployeService.update(id, request)` | 200 | 400 (données invalides) ; 404 (employé/agence absent) |
| EmployeController | DELETE | `/api/employes/{id}` | `IEmployeService.deleteById(id)` | 204 | 404 |
| EquipementController | GET | `/api/equipements` | `IEquipementService.findAll()` | 200 | — |
| EquipementController | GET | `/api/equipements/{id}` | `IEquipementService.findById(id)` | 200 | 404 |
| EquipementController | POST | `/api/equipements` | `IEquipementService.create(equipement)` | 201 | 400 (libellé invalide) |
| EquipementController | PUT | `/api/equipements/{id}` | `IEquipementService.update(id, equipement)` | 200 | 400 (libellé/identifiant invalide) ; 404 |
| EquipementController | DELETE | `/api/equipements/{id}` | `IEquipementService.deleteById(id)` | 204 | 404 |
| MaintenanceController | GET | `/api/maintenances` | `IMaintenanceService.findAll()` | 200 | — |
| MaintenanceController | GET | `/api/maintenances/{id}` | `IMaintenanceService.findById(id)` | 200 | 404 |
| MaintenanceController | POST | `/api/maintenances` | `IMaintenanceService.create(request)` | 201 | 400 (données invalides) ; 404 (véhicule absent) |
| MaintenanceController | PUT | `/api/maintenances/{id}` | `IMaintenanceService.update(id, request)` | 200 | 400 (données invalides) ; 404 (maintenance/véhicule absent) |
| MaintenanceController | DELETE | `/api/maintenances/{id}` | `IMaintenanceService.deleteById(id)` | 204 | 404 |
| ReservationController | GET | `/api/reservations` | `IReservationService.findAll()` | 200 | — |
| ReservationController | GET | `/api/reservations/{id}` | `IReservationService.findById(id)` | 200 | 404 |
| ReservationController | POST | `/api/reservations` | `IReservationService.create(request)` | 201 | 400 (données invalides) ; 404 (client/véhicule/contrat absent) |
| ReservationController | PUT | `/api/reservations/{id}` | `IReservationService.update(id, request)` | 200 | 400 (données invalides) ; 404 (réservation/client/véhicule/contrat absent) |
| ReservationController | DELETE | `/api/reservations/{id}` | `IReservationService.deleteById(id)` | 204 | 404 |
| VehiculeController | GET | `/api/vehicules` | `IVehiculeService.findAll()` | 200 | — |
| VehiculeController | GET | `/api/vehicules/{id}` | `IVehiculeService.findById(id)` | 200 | 404 |
| VehiculeController | POST | `/api/vehicules` | `IVehiculeService.create(request)` | 201 | 400 (données invalides) ; 404 (agence/équipement absent) |
| VehiculeController | PUT | `/api/vehicules/{id}` | `IVehiculeService.update(id, request)` | 200 | 400 (données invalides) ; 404 (véhicule/agence/équipement absent) |
| VehiculeController | DELETE | `/api/vehicules/{id}` | `IVehiculeService.deleteById(id)` | 204 | 404 |
| PaiementController | GET | `/api/paiements` | `IPaiementService.findAll()` | 200 | — |
| PaiementController | GET | `/api/paiements/{id}` | `IPaiementService.findById(id)` | 200 | 404 |

`GET /` est également déclaré dans `HomeController` : il redirige vers `/swagger-ui/index.html` et n'est pas un endpoint métier de l'API.

## B. Annotations Jackson

Les annotations Jackson contrôlent la sérialisation JSON des associations JPA afin d'éviter les cycles et d'adapter la représentation exposée par l'API.

- Dans `Contrat`, `@JsonManagedReference("contrat-paiements")` marque le côté parent de l'association avec les paiements. La liste `paiements` est incluse dans le JSON du contrat.
- Dans `Paiement`, `@JsonBackReference("contrat-paiements")` marque le côté inverse. Le champ `contrat` n'est pas resérialisé depuis chaque paiement : cela empêche la boucle contrat → paiements → contrat.
- `@JsonIgnore` exclut complètement une propriété du JSON. Le projet l'utilise notamment pour les liens `Reservation` vers `Vehicule`, `Client` et `Contrat`, ainsi que les associations inverses de `Employe`, `Vehicule`, `Equipement` et `Maintenance`. Cela évite d'exposer ces propriétés dans cette représentation et prévient également certaines récursions.

Ces annotations agissent sur la représentation JSON ; elles ne remplacent ni les règles d'accès métier ni la validation des données entrantes.

## C. Postman et Swagger

Postman sert à construire et envoyer des requêtes HTTP (méthode, URL, en-têtes et corps), puis à examiner les réponses afin de tester l'API. Swagger, fourni ici par SpringDoc, présente la documentation des routes et de leurs paramètres ; son interface Swagger UI permet aussi d'exécuter des requêtes depuis le navigateur. Postman est donc principalement un outil de test manuel, tandis que Swagger documente l'API et offre en plus une fonction d'essai.

## D. Anomalies SonarQube

Le signalement communiqué concerne la règle « Database Operations should not be vulnerable to mass assignment » et apparaît deux fois. Il s'agit de deux occurrences du même type de problème, pas de deux catégories SonarQube différentes. Les cas observés étaient :

1. **Client, agence et équipement directement liés au corps HTTP** : les contrôleurs recevaient des entités JPA, ce qui rendait leurs champs techniques ou leurs associations accessibles au binding JSON. **Correction appliquée :** ajout de `ClientRequest`, `AgenceRequest` et `EquipementRequest`, puis copie explicite des seuls champs autorisés vers les entités. La date d'inscription du client est attribuée côté serveur.
2. **Contrat directement lié au corps HTTP** : le binding sur `Contrat` permettait aussi de fournir des propriétés de gestion, notamment `valide` et les associations `reservation`/`paiements`. **Correction appliquée :** `ContratRequest` n'accepte que la date de signature et le montant. Le statut de validation et les relations ne sont plus modifiables par cette requête ; lors d'une mise à jour, la valeur `valide` déjà enregistrée est conservée.

Ces DTOs constituent une liste blanche des données modifiables et réduisent l'opération de persistance aux champs explicitement prévus par l'API.

## E. Questions de réflexion

1. **Quel est le rôle de `DispatcherServlet` ?**  
   C'est le contrôleur frontal de Spring MVC. Il reçoit les requêtes HTTP, les associe à un contrôleur via le mapping, fait résoudre les paramètres et le corps de la requête, puis délègue le traitement. Il coordonne ensuite la création de la réponse HTTP, notamment la sérialisation JSON.

2. **Pourquoi utiliser des interfaces pour les services ?**  
   Une interface définit le contrat du service sans coupler les contrôleurs à son implémentation. Spring injecte l'implémentation correspondante ; cette séparation facilite le remplacement d'une implémentation et l'isolation des services dans les tests.

3. **Pourquoi les codes HTTP sont-ils importants ?**  
   Ils indiquent de façon standardisée le résultat d'une requête : par exemple `200` pour une consultation ou modification réussie, `201` pour une création, `204` pour une suppression sans contenu, `400` pour une entrée invalide et `404` pour une ressource introuvable. Les clients peuvent réagir correctement sans interpréter uniquement le texte de la réponse.

4. **Pourquoi le contrôleur `Paiement` est-il en lecture seule ?**  
   `PaiementController` n'expose que les opérations de consultation (`GET`). Aucun endpoint HTTP de création, de modification ou de suppression de paiement n'est déclaré dans ce contrôleur ; l'API ne permet donc pas de modifier directement ces enregistrements par cette ressource.

5. **Quels sont les risques d'exposer directement les entités JPA ?**  
   Le binding direct peut permettre la mass assignment de champs sensibles (identifiants, statuts et relations), et la sérialisation peut révéler des données internes ou provoquer des références circulaires. Les entités exposent aussi les détails du modèle de persistance et peuvent causer des problèmes de chargement des associations. Des DTOs dédiés permettent de limiter les champs reçus et retournés à ceux prévus par l'API.
