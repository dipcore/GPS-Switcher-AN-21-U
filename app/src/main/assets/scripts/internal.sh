#!/system/bin/sh
# GPS Switcher
# internal.sh
# dipcore@gmail.com

LIB_STOCK=/system/lib/hw/gps.default.so
LIB_BACKUP=/system/lib/hw/gps.default.so.backup

echo "----------------------------"
echo `date`
echo "Switching to internal GPS"

mount -o rw,remount /system
# Clear old env params
sed -i -e 's/ro.kernel.android.gps.*//g' /system/build.prop
# Copy stock lib
cp $LIB_BACKUP $LIB_STOCK
chmod 644 $LIB_STOCK
chown root.root $LIB_STOCK
mount -o ro,remount /system

echo "Done"
echo "!!! REBOOT REQUIRED !!!"
echo "----------------------------"