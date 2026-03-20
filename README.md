# Aethryx Bot 
 
> **Projet personnel en cours de développement — instable, non destiné à une utilisation publique.**
 
Bot Discord personnel développé en Java avec [JDA](https://github.com/discord-jda/JDA).
 
---
 
## Commandes disponibles
 
| Commande | Description | Statut |
|---|---|---|
| `/ping` | Latence du bot | Fonctionnel |
| `/hello` | Le bot te salue | Fonctionnel |
| `/avatar` | Affiche l'avatar d'un utilisateur | Fonctionnel |
| `/serverinfo` | Infos sur le serveur | Fonctionnel |
| `/userinfo` | Infos sur un utilisateur | Fonctionnel |
 
---
 
## Stack
 
- Java 25
- [JDA 5.0.0-beta.20](https://github.com/discord-jda/JDA)
- Maven
 
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
mvn clean package
java -jar target/BOT_Aethryx.jar
```
 
---
 
## Roadmap
 
- [x] Modération (`/kick`, `/ban`, `/mute`)
- [ ] Système de warns
- [ ] Système de niveaux XP
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
