#!/bin/bash
STACK_NAME=MicronautAppStack
API_URL="$(aws cloudformation describe-stacks --stack-name $STACK_NAME --query 'Stacks[0].Outputs[?OutputKey==`ApiUrl`].OutputValue' --output text)"
RESPONSE="$(curl -s $API_URL)"
EXPECTED_RESPONSE='{"message":"Hello World"}'
if [ "$RESPONSE" != "$EXPECTED_RESPONSE" ]; then echo $RESPONSE && exit 1; fi
echo "success"
exit $EXIT_STATUS
