package contracts

import org.springframework.cloud.contract.spec.Contract

[
        Contract.make {
            request {
                url "/songs/1"
                method GET()
            }

            response {
                status OK()
                headers {
                    contentType applicationJson()
                }
                body(
                        "id": 1,
                        "name": "First",
                        "artist": "Second",
                        "album": "Third",
                        "length": "2:59",
                        "resourceId": 123,
                        "year": "2021"
                )
            }
        },
        Contract.make {
            request {
                url "/songs"
                method POST()
                headers {
                    contentType applicationJson()
                }
                body(
                        "name": "First",
                        "artist": "Second",
                        "album": "Third",
                        "length": "2:59",
                        "resourceId": 123,
                        "year": "2021"
                )
            }

            response {
                status OK()
                headers {
                    contentType applicationJson()
                }
                body(
                        "id": anyNumber()
                )
            }
        },
        Contract.make {
            request {
                url "/songs/1"
                method DELETE()
            }

            response {
                status OK()
                headers {
                    contentType applicationJson()
                }
                body(
                        "ids": 1
                )
            }
        }

]
