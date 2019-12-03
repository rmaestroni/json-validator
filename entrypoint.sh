#!/usr/bin/env bash

[[ -z $PORT ]] && echo 'No PORT variable has been provided, exiting' >&2 && exit 1
[[ -z $SCHEMA_DIR ]] && echo 'No SCHEMA_DIR variable has been provided, exiting' >&2 && exit 1

if [[ -z $@ ]]; then
  echo 'No API runner command has been provided, exiting' >&2
  exit 1
else
  exec $@
fi
