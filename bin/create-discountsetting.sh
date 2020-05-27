#!/usr/bin/env bash
set -x
echo "started"

curl http://localhost:8080/store/

#curl -ik -X POST \
#    -d "{\"discountCode\":\"89889\", \"discountAmount\":\"89\", \"minTransactionsRequired\":\"23\"}" \
#    "http://localhost:8080/store/discount"



