#!/bin/bash
rm original.txt returned.txt original2.txt Devon\'s\ Level SpecificB.txt
javac Test.java
java Test
DIFF=$(diff original.txt returned.txt)
if [ "$DIFF" == "" ]
then	
	echo "Code Transfer Test Passed"
else
	echo "Code Transfer Test Failed"
fi

DIFF=$(diff original2.txt Devon\'s\ Level)
if [ "$DIFF" == "" ]
then
	echo "Level Transfer Test Passed"
else
	echo "Code Transfer Test Failed"
fi

DIFF=$(diff SpecificA.txt SpecificB.txt)
if [ "$DIFF" == "" ]
then
	echo "Specific Code Transfer Test Passed"
else
	echo "Specific Code Transfer Test Failed"
fi
