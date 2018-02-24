#!/bin/bash

redis-server /usr/local/etc/redis.conf &
# mongod --config /usr/local/etc/mongod.conf &
/usr/local/sbin/rabbitmq-server &
mysql.server start

function killstuff {
  jobs -p | xargs kill
}

trap killstuff SIGINT

wait


