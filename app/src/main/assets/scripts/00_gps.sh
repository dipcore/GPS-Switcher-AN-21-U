#!/system/bin/sh
# GPS Switcher
# init.d 00_gps.sh
# dipcore@gmail.com

GPSTTY=/dev/`getprop ro.kernel.android.gps`
SPEED=`getprop ro.kernel.android.gps.speed`

if [[ ! -z $GPSTTY ]] ; then

	# Default speed
	if [[ -z $SPEED ]] ; then
		SPEED="9600"
	fi;
	
	# Serial device params
	if [ -c $GPSTTY ] ; then
		echo "Setting USB GPS $GPSTTY speed to $SPEED"
		# Speed
		busybox stty -F $GPSTTY ispeed $SPEED
		# No echo
		busybox stty -F $GPSTTY -echo
		# Owner
		chown root:system $GPSTTY
		# Permissions
		chmod 666 $GPSTTY
	fi;
	
fi;