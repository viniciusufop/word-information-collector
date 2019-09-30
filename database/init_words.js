db.createUser(
    {
        user: "words",
        pwd: "1234",
        roles: [
            {
                role: "readWrite",
                db: "words"
            }
        ]
    }
);
