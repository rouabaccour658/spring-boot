# Documentation fonctionnelle d'AutoLoc

## Présentation

AutoLoc est une API REST Spring Boot destinée à la gestion d'une agence de location de véhicules. Le modèle couvre les agences, les employés, les véhicules, les équipements, les clients, les réservations, les contrats, les paiements et les maintenances.

Le projet utilise Java 17, Spring Web, Spring Data JPA et MySQL. L'application écoute sur le port `8081`. L'adresse `http://localhost:8081/` redirige vers Swagger UI. Les routes de l'API conservent le préfixe `/api`. La base configurée pour l'exécution normale est `autoloc_db` sur un serveur MySQL local.

## Fonctionnalités actuellement accessibles par HTTP

Les contrôleurs des entités exposent les routes REST suivantes. Le préfixe commun est `/api`.

| Méthode | URL | Fonction |
|---|---|---|
| `GET` | `/api/agences` | Retourne la liste des agences. |
| `GET` | `/api/agences/{id}` | Recherche une agence par son identifiant. |
| `POST` | `/api/agences` | Crée une agence et retourne l'objet créé avec le statut HTTP `201`. |
| `PUT` | `/api/agences/{id}` | Modifie le nom, la ville, l'adresse et le téléphone de l'agence. |
| `DELETE` | `/api/agences/{id}` | Supprime l'agence et retourne le statut HTTP `204`. |
| `GET` | `/api/employes` | Retourne la liste des employés. |
| `GET` | `/api/employes/{id}` | Recherche un employé par son identifiant. |
| `POST` | `/api/employes` | Crée un employé. Le champ `agenceId` est facultatif. |
| `PUT` | `/api/employes/{id}` | Modifie un employé et son agence éventuelle. |
| `DELETE` | `/api/employes/{id}` | Supprime un employé. |
| `GET` | `/api/vehicules` | Retourne la liste des véhicules. |
| `GET` | `/api/vehicules/{id}` | Recherche un véhicule par son identifiant. |
| `POST` | `/api/vehicules` | Crée un véhicule avec une agence facultative et des identifiants d'équipements. |
| `PUT` | `/api/vehicules/{id}` | Modifie un véhicule et ses associations agence/équipements. |
| `DELETE` | `/api/vehicules/{id}` | Supprime un véhicule. |
| `GET` | `/api/equipements` | Retourne la liste des équipements. |
| `GET` | `/api/equipements/{id}` | Recherche un équipement par son identifiant. |
| `POST` | `/api/equipements` | Crée un équipement. |
| `PUT` | `/api/equipements/{id}` | Modifie le libellé d'un équipement. |
| `DELETE` | `/api/equipements/{id}` | Supprime l'équipement et le retire des véhicules associés. |
| `GET` | `/api/clients` | Retourne la liste des clients. |
| `GET` | `/api/clients/{id}` | Recherche un client par son identifiant. |
| `POST` | `/api/clients` | Crée un client. |
| `PUT` | `/api/clients/{id}` | Modifie les informations du client. |
| `DELETE` | `/api/clients/{id}` | Supprime un client. |
| `GET` | `/api/reservations` | Retourne la liste des réservations. |
| `GET` | `/api/reservations/{id}` | Recherche une réservation par son identifiant. |
| `POST` | `/api/reservations` | Crée une réservation avec les identifiants du client, du véhicule et éventuellement du contrat. |
| `PUT` | `/api/reservations/{id}` | Modifie une réservation et ses associations. |
| `DELETE` | `/api/reservations/{id}` | Supprime une réservation. |
| `GET` | `/api/contrats` | Retourne la liste des contrats. |
| `GET` | `/api/contrats/{id}` | Recherche un contrat par son identifiant. |
| `POST` | `/api/contrats` | Crée un contrat et retourne l'objet créé avec le statut HTTP `201`. |
| `PUT` | `/api/contrats/{id}` | Modifie la date de signature, le montant et l'état de validation du contrat. |
| `DELETE` | `/api/contrats/{id}` | Supprime le contrat et retourne le statut HTTP `204`. |
| `GET` | `/api/paiements` | Retourne la liste des paiements. |
| `GET` | `/api/paiements/{id}` | Recherche un paiement par son identifiant. |
| `GET` | `/api/maintenances` | Retourne la liste des maintenances. |
| `GET` | `/api/maintenances/{id}` | Recherche une maintenance par son identifiant. |
| `POST` | `/api/maintenances` | Crée une maintenance liée à un véhicule. |
| `PUT` | `/api/maintenances/{id}` | Modifie une maintenance et son véhicule. |
| `DELETE` | `/api/maintenances/{id}` | Supprime une maintenance. |

Exemple de création :

```http
POST /api/contrats
Content-Type: application/json
```

```json
{
  "dateSignature": "2026-10-22",
  "montantTotal": 250.00,
  "valide": false
}
```

La création refuse un contrat qui possède déjà un identifiant. Le montant total ne peut pas être négatif. Les dates utilisent le format ISO `AAAA-MM-JJ`.

Les requêtes de véhicule utilisent `agenceId` et `equipementIds` pour les associations, plutôt que des objets imbriqués. Les requêtes de réservation utilisent les identifiants réels de son modèle :

```json
{
  "dateDebut": "2026-11-01",
  "dateFin": "2026-11-05",
  "statut": "EN_ATTENTE",
  "vehiculeId": 1,
  "clientId": 1,
  "contratId": null
}
```

Le véhicule, le client, le statut et les dates sont obligatoires pour une réservation ; la date de fin doit être postérieure à la date de début. `contratId` est facultatif. Pour un véhicule, les champs obligatoires sont `immatriculation`, `marque`, `modele`, `categorie`, `tarifJournalier` et `statut`; `agenceId` est facultatif et `equipementIds` peut être omis ou être une liste vide.

## Fonctionnalités disponibles dans les services

Les services des agences, employés, véhicules, équipements, clients, réservations, contrats et maintenances fournissent les opérations CRUD via leurs routes respectives. Le service des paiements fournit uniquement la lecture, conformément au périmètre actuel de son contrôleur.

Le service des contrats (`IContratService`) permet de créer, lister, rechercher, modifier et supprimer des contrats. La modification met à jour la date de signature, le montant total et l'état de validation. Une recherche ou suppression portant sur un identifiant inexistant déclenche `ResourceNotFoundException`.

Les ressources introuvables sont retournées par l'API avec le statut HTTP `404`. Les requêtes invalides, dont un montant de contrat négatif ou un identifiant du corps qui ne correspond pas à l'URL, sont retournées avec le statut HTTP `400`.

## Modèle métier

| Entité | Rôle et principales données |
|---|---|
| `Agence` | Agence de location, avec nom, ville, adresse et téléphone. Elle regroupe des employés et des véhicules. |
| `Employe` | Employé rattaché à une agence, avec nom, prénom et rôle (`AGENT` ou `MANAGER`). |
| `Vehicule` | Véhicule identifié notamment par son immatriculation, sa marque, son modèle, sa catégorie, son tarif journalier et son statut. Il est associé à une agence, à des réservations, des maintenances et des équipements. |
| `Equipement` | Équipement caractérisé par un libellé et pouvant être associé à plusieurs véhicules. |
| `Client` | Client avec nom, prénom, adresse e-mail, téléphone, numéro de permis et date d'inscription. |
| `Reservation` | Période de location avec dates de début et de fin, statut, véhicule, client et contrat associé. |
| `Contrat` | Contrat avec date de signature, montant total et indicateur de validation. Il peut être associé à une réservation et à plusieurs paiements. |
| `Paiement` | Paiement avec montant, date et mode de paiement, rattaché à un contrat. |
| `Maintenance` | Intervention sur un véhicule, avec dates de début et de fin et description. |

Les statuts et catégories possibles sont définis par des énumérations :

- Catégories de véhicule : `CITADINE`, `BERLINE`, `SUV`, `UTILITAIRE`.
- Statuts de véhicule : `DISPONIBLE`, `LOUE`, `MAINTENANCE`.
- Statuts de réservation : `EN_ATTENTE`, `CONFIRMEE`, `ANNULEE`, `TERMINEE`.
- Modes de paiement : `CARTE`, `ESPECES`, `VIREMENT`.
- Rôles d'employé : `AGENT`, `MANAGER`.

## Relations et sérialisation

Les relations JPA décrivent les associations entre les entités : une agence regroupe des employés et des véhicules, un client peut avoir plusieurs réservations, un véhicule peut avoir plusieurs réservations et maintenances, et un contrat peut avoir plusieurs paiements.

Les cascades de persistance sont configurées du parent vers ses éléments dépendants. La suppression d'un contrat supprime ses paiements (`orphanRemoval`) et détache d'abord sa réservation pour préserver la contrainte de clé étrangère. Les références `@ManyToOne` et les références enfant vers leur parent ne propagent pas les opérations : supprimer un paiement seul ne supprime pas son contrat. Les équipements étant partageables entre véhicules, l'association ne propage que la création et la mise à jour, pas la suppression.

Les références de retour sont ignorées dans le JSON afin d'éviter les cycles lors de la sérialisation des relations bidirectionnelles. Les contrats peuvent ainsi inclure leur réservation et leurs paiements sans que ceux-ci réincluent le contrat en boucle.

## Persistance et démarrage

L'exécution normale utilise MySQL à `127.0.0.1:3306`, avec le schéma `autoloc_db`. L'hôte, le port, le schéma, l'utilisateur et le mot de passe peuvent être définis avec les variables `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USERNAME` et `DB_PASSWORD`. Par défaut, l'application utilise `root` avec un mot de passe vide. Le compte MySQL/MariaDB doit être autorisé à se connecter depuis la machine de l'application et à accéder au schéma. Hibernate met à jour le schéma au démarrage (`spring.jpa.hibernate.ddl-auto=update`) et l'affichage des requêtes SQL est activé.

Pour l'erreur MariaDB `Host 'localhost' is not allowed to connect`, il faut autoriser le compte côté serveur MariaDB (à exécuter avec un compte administrateur MariaDB) :

```sql
CREATE DATABASE IF NOT EXISTS autoloc_db;
CREATE USER IF NOT EXISTS 'autoloc'@'localhost' IDENTIFIED BY 'choisir-un-mot-de-passe';
CREATE USER IF NOT EXISTS 'autoloc'@'127.0.0.1' IDENTIFIED BY 'choisir-un-mot-de-passe';
GRANT ALL PRIVILEGES ON autoloc_db.* TO 'autoloc'@'localhost';
GRANT ALL PRIVILEGES ON autoloc_db.* TO 'autoloc'@'127.0.0.1';
```

Puis définir `DB_USERNAME=autoloc` et `DB_PASSWORD` avec le mot de passe choisi dans la configuration d'exécution de l'application. Le message `Unable to determine Dialect` disparaîtra une fois la connexion JDBC autorisée ; ce message est une conséquence du refus de connexion, pas un dialecte à ajouter manuellement.

Les tests utilisent une base H2 en mémoire définie dans `src/test/resources/application.properties`. Ils ne nécessitent donc pas de serveur MySQL.

Depuis le dossier `tp-autoloc`, après avoir démarré MySQL, lancer l'application avec :

```bash
mvn spring-boot:run
```

Les repositories Spring Data JPA fournissent les opérations standard de persistance (`save`, `findById`, `findAll`, `existsById`, `deleteById`). Les autres entités possèdent leurs repositories, mais ne disposent pas encore de services ni d'endpoints REST.

## Tests présents

Le projet contient des tests Spring Boot de démarrage et un test du service d'agence qui vérifie notamment la création, la recherche, le comptage et la suppression d'une agence.
