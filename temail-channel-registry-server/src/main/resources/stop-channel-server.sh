#!/bin/bash
ps -ef|grep temail-channel | grep -v "grep" | awk '{print $2}' | xargs kill -9