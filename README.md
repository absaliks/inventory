# Getting Started

Get list of phones: `curl --location --request GET 'http://localhost:8080/inventory'`
```
Response: [
    {
        "id": 1,
        "phoneName": "Samsung Galaxy S9",
        "user": "John Doe",
        "lendDate": "2020-11-29T09:47:58.588543Z"
    },
    {
        "id": 2,
        "phoneName": "Samsung Galaxy S8",
        "user": null,
        "lendDate": null
    },
    ...
]
```

In order to borrow a phone user should PATCH user field of the inventory resource:
```
curl --location --request PATCH 'http://localhost:8080/inventory/2' \
--header 'Content-Type: application/json' \
--data-raw '{ "user": "Jane Doe" }'
```
```
Response: {
    "id": 2,
    "phoneName": "Samsung Galaxy S8",
    "user": "Jane Doe",
    "lendDate": "2020-11-29T09:48:14.758461Z"
}
```