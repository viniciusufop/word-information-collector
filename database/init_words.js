db.createUser(
    {
        user: "word-information-collector",
        pwd: "1234",
        roles: [
            {
                role: "readWrite",
                db: "words"
            }
        ]
    }
);
