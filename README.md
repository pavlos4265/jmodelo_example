## jmodelo_example

This is a simple web app, using jmodelo and htmx, where users can create accounts and post statuses.

SQLite has been used for the database. Its schema is in db.sql. To create the database, install
sqlite and run:
```
sqlite3 social.db < db.sql
```

This app serves as an example, and the user passwords are stored as plain text.

To build the app, include the jmodelo jar.
