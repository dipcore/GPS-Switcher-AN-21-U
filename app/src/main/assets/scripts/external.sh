#!/system/bin/sh
# GPS Switcher
# external.sh
# dipcore@gmail.com

LIB_STOCK=/system/lib/hw/gps.default.so
DIR=`dirname $0`

echo "----------------------------"
echo `date`
echo "Switching to external GPS"

if [[ -z $1 ]] || [[ -z $2 ]] || [[ -z $3 ]] ; then
	echo "Usage: external.sh *libray* *device* *speed*"
else

	echo "Library: $1"
	echo "Device: $2"
	echo "Speed: $3"
	
	mount -o rw,remount /system

	# Clear old env params
	sed -i -e 's/ro.kernel.android.gps.*//g' /system/build.prop

	# Add new env params
	echo -e "\nro.kernel.android.gps=$2" >> /system/build.prop
	echo -e "ro.kernel.android.gps.speed=$3" >> /system/build.prop

	# Copy library
	if [[ $1 == "generic_gnss" ]]; then
		cp $DIR/generic_gnss.so $LIB_STOCK
	fi;
	if [[ $1 == "generic" ]]; then
		cp $DIR/generic.so $LIB_STOCK
	fi;
	if [[ $1 == "mst_patched" ]]; then
		sed -e "s/\\/dev\\/ttyAAA0/$1/g" $DIR/mst_patched.so > $LIB_STOCK
	fi;
	
	chmod 644 $LIB_STOCK
	chown root.root $LIB_STOCK

	mount -o ro,remount /system

	echo "Done"
	echo "!!! REBOOT REQUIRED !!!"
fi;
echo "----------------------------"

