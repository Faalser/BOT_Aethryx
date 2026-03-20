# Aethryx Bot 
 
> **Projet personnel en cours de développement — instable, non destiné à une utilisation publique.**
 
Bot Discord multifonction développé en Java avec [JDA](https://github.com/discord-jda/JDA).
Modération, utilitaire, fun et bien plus à venir !
 
[![Inviter le bot](https://img.shields.io/badge/Inviter%20Aethryx-5865F2?style=for-the-badge&logo=discord&logoColor=white)](https://discord.com/oauth2/authorize?client_id=1484677497445679114&permissions=8&integration_type=0&scope=bot+applications.commands)
 
---
 
## Commandes disponibles
 
### 🛠️ Utilitaire
| Commande | Description | Statut |
|---|---|---|
| `/ping` | Latence du bot | <ul><li>[x] Fonctionnel</li></ul>|
| `/hello` | Le bot te salue | <ul><li>[x] Fonctionnel</li></ul>|
| `/avatar` | Affiche l'avatar d'un utilisateur | <ul><li>[x] Fonctionnel</li></ul>|
| `/serverinfo` | Infos sur le serveur | <ul><li>[x] Fonctionnel</li></ul>|
| `/userinfo` | Infos sur un utilisateur | <ul><li>[x] Fonctionnel</li></ul>|
| `/help` | Affiche la liste des commandes | <ul><li>[x] Fonctionnel</li></ul>|
 
### 🔨 Modération
| Commande | Description | Statut |
|---|---|---|
| `/kick` | Expulser un membre | <ul><li>[x] Fonctionnel</li></ul>|
| `/ban` | Bannir un membre | <ul><li>[x] Fonctionnel</li></ul>|
| `/mute` | Rendre muet un membre | <ul><li>[x] Fonctionnel</li></ul>|
| `/unmute` | Retirer le mute d'un membre | <ul><li>[x] Fonctionnel</li></ul>|
| `/clear` | Supprimer des messages | <ul><li>[x] Fonctionnel</li></ul>|
 
---
 
## Stack
 
- Java 25
- [JDA 5.0.0-beta.20](https://github.com/discord-jda/JDA)
- Maven
- PostgreSQL
 
---
 
## Installation
 
```bash
git clone https://github.com/faalser/BOT_Aethryx.git
cd BOT_Aethryx
```
 
Crée un fichier `.env` à la racine :
```
DISCORD_BOT_TOKEN=ton_token_ici
```
 
Lance le bot :
```bash
mvn clean package -DskipTests
java -jar target/Aethryx-1.0-SNAPSHOT.jar
```
 
---
 
## Roadmap
 
- [ ] Système de warns
- [ ] Système de niveaux XP
- [ ] Économie (monnaie virtuelle)
- [ ] Musique
- [ ] Giveaway
- [ ] Tickets support
 
---
 
## Licence
 
Ce projet est sous **licence personnalisée** — voir le fichier [LICENSE](LICENSE) pour plus de détails.
 
- [x] Utilisation et modification autorisées
- [x] Forks autorisés mais doivent rester open source
- [x] L'auteur original doit être crédité
- [ ] Toute utilisation commerciale est strictement interdite
 
---
 
## Auteur
 
**faalser** — [GitHub](https://github.com/faalser)
 
*Projet personnel — développement en cours*
