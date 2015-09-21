#!/system/bin/sh
# GPS Switcher
# start.sh
# dipcore@gmail.com

LIB_STOCK=/system/lib/hw/gps.default.so
LIB_BACKUP=/system/lib/hw/gps.default.so.backup
DIR=`dirname $0`

echo "----------------------------"
echo "Developed by dipcore"
echo "dipcore@gmail.com"
echo "----------------------------"
echo `date`

# Check if we have a backup of stock library
if [ ! -f $LIB_BACKUP ] ; then
	mount -o rw,remount /system
    echo "Backup not found, creating ..."
    cp $LIB_STOCK $LIB_BACKUP
	mount -o ro,remount /system
fi;

# Update init.d script
if [[ ! -f /system/etc/init.d/00_gps ]] || [[ `md5 /system/etc/init.d/00_gps | awk '{print $1}'` != "890f19107dcbc97d5d545be0887ce52a" ]] ; then 
	echo "Updating init.d script ..."
	mount -o rw,remount /system
	cp $DIR/00_gps.sh /system/etc/init.d/00_gps
	chmod 755 /system/etc/init.d/00_gps
	chown root.root /system/etc/init.d/00_gps
	mount -o ro,remount /system
fi;

# Checking status
STATUS_TTY=`getprop ro.kernel.android.gps`
STATUS_SPEED=`getprop ro.kernel.android.gps.speed`

if [[ -z "$STATUS_TTY" ]] || [[ -z "$STATUS_SPEED" ]] ; then

	if [[ `cat /system/lib/hw/gps.default.so | grep tty` == "/dev/ttyMbx4" ]] ; then
		STATUS_LIB=stock
		STATUS_TTY=ttyMbx4
		STATUS_SPEED=9600
	fi;
	
else

	if [[ `md5 /system/lib/hw/gps.default.so | awk '{print $1}'` == "3de0bccdeda7f9b0be0e76bb9131ed70" ]] ; then 
		STATUS_LIB=generic
	else
		if [[ `md5 /system/lib/hw/gps.default.so | awk '{print $1}'` == "d7c53603c57b8dc503d41e4f7e61582d" ]] ; then
			STATUS_LIB=generic_gnss
		else
			if [[ -n `cat /system/lib/hw/gps.default.so | grep ttyUSB` ]] ; then
				STATUS_LIB=mst_pathed
			else
				STATUS_LIB=unknown
			fi;
		fi;
	fi;
	
fi;

echo "Current status:"
echo "Library: $STATUS_LIB"
echo "Device: $STATUS_TTY"
echo "Speed: $STATUS_SPEED"
echo "----------------------------"