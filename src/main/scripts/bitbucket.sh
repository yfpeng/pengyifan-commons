#!/bin/bash
#
# A small command that computes "Authorization" HTTP-header for basic
# authentication.

read -p "Username: " username
read -s -p "Password: " password
echo ""
encoded=`echo -n "$username:$password" | base64`
echo "BASIC $encoded"
