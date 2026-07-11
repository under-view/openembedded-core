SUMMARY = "Merge machine and distro options to create a basic machine task/package"

#
# packages which content depend on MACHINE_FEATURES need to be MACHINE_ARCH
#
PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

PACKAGES = ' \
            packagegroup-base \
            packagegroup-base-extended \
            packagegroup-distro-base \
            packagegroup-machine-base \
            \
            ${@bb.utils.contains("MACHINE_FEATURES", "acpi", "packagegroup-base-acpi", "",d)} \
            ${@bb.utils.contains("MACHINE_FEATURES", "alsa", "packagegroup-base-alsa", "", d)} \
            ${@bb.utils.contains("MACHINE_FEATURES", "ext2", "packagegroup-base-ext2", "", d)} \
            ${@bb.utils.contains("MACHINE_FEATURES", "vfat", "packagegroup-base-vfat", "", d)} \
            ${@bb.utils.contains("MACHINE_FEATURES", "keyboard", "packagegroup-base-keyboard", "", d)} \
            ${@bb.utils.contains("MACHINE_FEATURES", "pci", "packagegroup-base-pci", "",d)} \
            ${@bb.utils.contains("MACHINE_FEATURES", "pcmcia", "packagegroup-base-pcmcia", "", d)} \
            ${@bb.utils.contains("MACHINE_FEATURES", "phone", "packagegroup-base-phone", "", d)} \
            ${@bb.utils.contains("MACHINE_FEATURES", "serial", "packagegroup-base-serial", "", d)} \
            ${@bb.utils.contains("MACHINE_FEATURES", "usbgadget", "packagegroup-base-usbgadget", "", d)} \
            ${@bb.utils.contains("MACHINE_FEATURES", "usbhost", "packagegroup-base-usbhost", "", d)} \
            \
            ${@bb.utils.contains("DISTRO_FEATURES", "bluetooth", "packagegroup-base-bluetooth", "", d)} \
            ${@bb.utils.contains("DISTRO_FEATURES", "wifi", "packagegroup-base-wifi", "", d)} \
            ${@bb.utils.contains("DISTRO_FEATURES", "3g", "packagegroup-base-3g", "", d)} \
            ${@bb.utils.contains("DISTRO_FEATURES", "nfc", "packagegroup-base-nfc", "", d)} \
            ${@bb.utils.contains("DISTRO_FEATURES", "cramfs", "packagegroup-base-cramfs", "", d)} \
            ${@bb.utils.contains("DISTRO_FEATURES", "ipsec", "packagegroup-base-ipsec", "", d)} \
            ${@bb.utils.contains("DISTRO_FEATURES", "ipv6", "packagegroup-base-ipv6", "", d)} \
            ${@bb.utils.contains("DISTRO_FEATURES", "nfs", "packagegroup-base-nfs", "", d)} \
            ${@bb.utils.contains("DISTRO_FEATURES", "ppp", "packagegroup-base-ppp", "", d)} \
            ${@bb.utils.contains("DISTRO_FEATURES", "smbfs", "packagegroup-base-smbfs", "", d)} \
            ${@bb.utils.contains("DISTRO_FEATURES", "zeroconf", "packagegroup-base-zeroconf", "", d)} \
            \
            '

#
# packagegroup-base contain stuff needed for base system (machine related)
#
RDEPENDS:packagegroup-base = "\
    packagegroup-distro-base \
    packagegroup-machine-base \
    \
    module-init-tools \
    ${@bb.utils.contains('MACHINE_FEATURES', 'acpi', 'packagegroup-base-acpi', '',d)} \
    ${@bb.utils.contains('MACHINE_FEATURES', 'keyboard', 'packagegroup-base-keyboard', '',d)} \
    ${@bb.utils.contains('MACHINE_FEATURES', 'phone', 'packagegroup-base-phone', '',d)} \
    ${@bb.utils.contains('MACHINE_FEATURES', 'pcmcia', 'packagegroup-base-pcmcia', '',d)} \
    ${@bb.utils.contains('MACHINE_FEATURES', 'usbgadget', 'packagegroup-base-usbgadget', '',d)} \
    ${@bb.utils.contains('MACHINE_FEATURES', 'usbhost', 'packagegroup-base-usbhost', '',d)} \
    \
    ${@bb.utils.contains('COMBINED_FEATURES', 'alsa', 'packagegroup-base-alsa', '',d)} \
    ${@bb.utils.contains('COMBINED_FEATURES', 'ext2', 'packagegroup-base-ext2', '',d)} \
    ${@bb.utils.contains('COMBINED_FEATURES', 'vfat', 'packagegroup-base-vfat', '',d)} \
    ${@bb.utils.contains('COMBINED_FEATURES', 'pci', 'packagegroup-base-pci', '',d)} \
    ${@bb.utils.contains('COMBINED_FEATURES', 'bluetooth', 'packagegroup-base-bluetooth', '',d)} \
    ${@bb.utils.contains('COMBINED_FEATURES', 'wifi', 'packagegroup-base-wifi', '',d)} \
    ${@bb.utils.contains('COMBINED_FEATURES', '3g', 'packagegroup-base-3g', '',d)} \
    ${@bb.utils.contains('COMBINED_FEATURES', 'nfc', 'packagegroup-base-nfc', '',d)} \
    \
    ${@bb.utils.contains('DISTRO_FEATURES', 'nfs', 'packagegroup-base-nfs', '',d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'cramfs', 'packagegroup-base-cramfs', '',d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'smbfs', 'packagegroup-base-smbfs', '',d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'ipv6', 'packagegroup-base-ipv6', '',d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'ipsec', 'packagegroup-base-ipsec', '',d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'ppp', 'packagegroup-base-ppp', '',d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'zeroconf', 'packagegroup-base-zeroconf', '',d)} \
    "

RRECOMMENDS:packagegroup-base = "\
    ${KERNEL_PACKAGE_NAME}-module-nls-utf8 \
    ${KERNEL_PACKAGE_NAME}-module-input \
    ${KERNEL_PACKAGE_NAME}-module-uinput \
    ${KERNEL_PACKAGE_NAME}-module-rtc-dev \
    ${KERNEL_PACKAGE_NAME}-module-rtc-proc \
    ${KERNEL_PACKAGE_NAME}-module-rtc-sysfs \
    ${KERNEL_PACKAGE_NAME}-module-unix"

RDEPENDS:packagegroup-base-extended = "\
    packagegroup-base \
    ${ADD_WIFI} \
    ${ADD_BT} \
    ${ADD_3G} \
    ${ADD_NFC} \
    "

ADD_WIFI = ""
ADD_BT = ""
ADD_3G = ""
ADD_NFC = ""

python __anonymous () {
    # If Distro want wifi and machine feature wifi/pci/pcmcia/usbhost (one of them)
    # then include packagegroup-base-wifi in packagegroup-base

    distro_features = set(d.getVar("DISTRO_FEATURES").split())
    machine_features= set(d.getVar("MACHINE_FEATURES").split())

    if "bluetooth" in distro_features and not "bluetooth" in machine_features and ("pcmcia" in machine_features or "pci" in machine_features or "usbhost" in machine_features):
        d.setVar("ADD_BT", "${MLPREFIX}packagegroup-base-bluetooth")

    if "wifi" in distro_features and not "wifi" in machine_features and ("pcmcia" in machine_features or "pci" in machine_features or "usbhost" in machine_features):
        d.setVar("ADD_WIFI", "${MLPREFIX}packagegroup-base-wifi")

    if "3g" in distro_features and not "3g" in machine_features and ("pcmcia" in machine_features or "pci" in machine_features or "usbhost" in machine_features):
        d.setVar("ADD_3G", "${MLPREFIX}packagegroup-base-3g")

    if "nfc" in distro_features and not "nfc" in machine_features and ("usbhost" in machine_features):
        d.setVar("ADD_NFC", "${MLPREFIX}packagegroup-base-nfc")
}

#
# packages added by distribution
#
SUMMARY:packagegroup-distro-base = "${DISTRO} extras"
DEPENDS:packagegroup-distro-base = "${DISTRO_EXTRA_DEPENDS}"
RDEPENDS:packagegroup-distro-base = "${DISTRO_EXTRA_RDEPENDS}"
RRECOMMENDS:packagegroup-distro-base = "${DISTRO_EXTRA_RRECOMMENDS}"

#
# packages added by machine config
#
SUMMARY:packagegroup-machine-base = "Extra packages required to fully support ${MACHINE} hardware"
RDEPENDS:packagegroup-machine-base = "${MACHINE_EXTRA_RDEPENDS}"
RRECOMMENDS:packagegroup-machine-base = "${MACHINE_EXTRA_RRECOMMENDS}"

SUMMARY:packagegroup-base-keyboard = "Keyboard support"
RDEPENDS:packagegroup-base-keyboard = "\
    ${VIRTUAL-RUNTIME_keymaps}"

SUMMARY:packagegroup-base-pci = "PCI bus support"
RDEPENDS:packagegroup-base-pci = "\
    pciutils"

SUMMARY:packagegroup-base-acpi = "ACPI support"
RDEPENDS:packagegroup-base-acpi = "\
    acpid"

SUMMARY:packagegroup-base-ext2 = "ext2 filesystem support"
RDEPENDS:packagegroup-base-ext2 = "\
    e2fsprogs-e2fsck \
    e2fsprogs-mke2fs"

RRECOMMENDS:packagegroup-base-ext2 = "\
    hdparm \
    e2fsprogs"

SUMMARY:packagegroup-base-vfat = "FAT filesystem support"
RRECOMMENDS:packagegroup-base-vfat = "\
    ${KERNEL_PACKAGE_NAME}-module-msdos \
    ${KERNEL_PACKAGE_NAME}-module-vfat \
    ${KERNEL_PACKAGE_NAME}-module-nls-iso8859-1 \
    ${KERNEL_PACKAGE_NAME}-module-nls-cp437 \
    dosfstools"

SUMMARY:packagegroup-base-alsa = "ALSA sound support"
RDEPENDS:packagegroup-base-alsa = "\
    alsa-utils-alsactl \
    alsa-utils-amixer \
    ${VIRTUAL-RUNTIME_alsa-state}"

RRECOMMENDS:packagegroup-base-alsa = "\
    ${KERNEL_PACKAGE_NAME}-module-snd-mixer-oss \
    ${KERNEL_PACKAGE_NAME}-module-snd-pcm-oss"

SUMMARY:packagegroup-base-pcmcia = "PC card slot support"
RDEPENDS:packagegroup-base-pcmcia = "\
    pcmciautils \
    "

RRECOMMENDS:packagegroup-base-pcmcia = "\
    ${KERNEL_PACKAGE_NAME}-module-pcmcia \
    ${KERNEL_PACKAGE_NAME}-module-airo-cs \
    ${KERNEL_PACKAGE_NAME}-module-pcnet-cs \
    ${KERNEL_PACKAGE_NAME}-module-serial-cs \
    ${KERNEL_PACKAGE_NAME}-module-ide-cs \
    ${KERNEL_PACKAGE_NAME}-module-ide-disk \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wifi', '${KERNEL_PACKAGE_NAME}-module-hostap-cs', '',d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wifi', '${KERNEL_PACKAGE_NAME}-module-orinoco-cs', '',d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wifi', '${KERNEL_PACKAGE_NAME}-module-spectrum-cs', '',d)}"

SUMMARY:packagegroup-base-bluetooth = "Bluetooth support"
RDEPENDS:packagegroup-base-bluetooth = "\
    bluez5 \
    "

RRECOMMENDS:packagegroup-base-bluetooth = "\
    ${KERNEL_PACKAGE_NAME}-module-bluetooth \
    ${KERNEL_PACKAGE_NAME}-module-l2cap \
    ${KERNEL_PACKAGE_NAME}-module-rfcomm \
    ${KERNEL_PACKAGE_NAME}-module-hci-vhci \
    ${KERNEL_PACKAGE_NAME}-module-bnep \
    ${KERNEL_PACKAGE_NAME}-module-hidp \
    ${KERNEL_PACKAGE_NAME}-module-hci-uart \
    ${KERNEL_PACKAGE_NAME}-module-sco \
    ${@bb.utils.contains('MACHINE_FEATURES', 'usbhost', '${KERNEL_PACKAGE_NAME}-module-hci-usb', '',d)} \
    ${@bb.utils.contains('MACHINE_FEATURES', 'pcmcia', '${KERNEL_PACKAGE_NAME}-module-bluetooth3c-cs', '',d)} \
    ${@bb.utils.contains('MACHINE_FEATURES', 'pcmcia', '${KERNEL_PACKAGE_NAME}-module-bluecard-cs', '',d)} \
    ${@bb.utils.contains('MACHINE_FEATURES', 'pcmcia', '${KERNEL_PACKAGE_NAME}-module-bluetoothuart-cs', '',d)} \
    ${@bb.utils.contains('MACHINE_FEATURES', 'pcmcia', '${KERNEL_PACKAGE_NAME}-module-dtl1-cs', '',d)} \
    "

SUMMARY:packagegroup-base-usbgadget = "USB gadget support"
RRECOMMENDS:packagegroup-base-usbgadget = "\
    ${KERNEL_PACKAGE_NAME}-module-pxa27x_udc \
    ${KERNEL_PACKAGE_NAME}-module-gadgetfs \
    ${KERNEL_PACKAGE_NAME}-module-g-file-storage \
    ${KERNEL_PACKAGE_NAME}-module-g-serial \
    ${KERNEL_PACKAGE_NAME}-module-g-ether"

SUMMARY:packagegroup-base-usbhost = "USB host support"
RDEPENDS:packagegroup-base-usbhost = "\
    usbutils "

RRECOMMENDS:packagegroup-base-usbhost = "\
    ${KERNEL_PACKAGE_NAME}-module-uhci-hcd \
    ${KERNEL_PACKAGE_NAME}-module-ohci-hcd \
    ${KERNEL_PACKAGE_NAME}-module-ehci-hcd \
    ${KERNEL_PACKAGE_NAME}-module-usbcore \
    ${KERNEL_PACKAGE_NAME}-module-usbhid \
    ${KERNEL_PACKAGE_NAME}-module-usbnet \
    ${KERNEL_PACKAGE_NAME}-module-sd-mod \
    ${KERNEL_PACKAGE_NAME}-module-scsi-mod \
    ${KERNEL_PACKAGE_NAME}-module-usbmouse \
    ${KERNEL_PACKAGE_NAME}-module-mousedev \
    ${KERNEL_PACKAGE_NAME}-module-usbserial \
    ${KERNEL_PACKAGE_NAME}-module-usb-storage "

SUMMARY:packagegroup-base-ppp = "PPP dial-up protocol support"
RDEPENDS:packagegroup-base-ppp = "\
    ppp \
    ppp-dialin"

RRECOMMENDS:packagegroup-base-ppp = "\
    ${KERNEL_PACKAGE_NAME}-module-ppp-async \
    ${KERNEL_PACKAGE_NAME}-module-ppp-deflate \
    ${KERNEL_PACKAGE_NAME}-module-ppp-generic \
    ${KERNEL_PACKAGE_NAME}-module-ppp-mppe \
    ${KERNEL_PACKAGE_NAME}-module-slhc"

SUMMARY:packagegroup-base-ipsec = "IPSEC support"
RDEPENDS:packagegroup-base-ipsec = "\
    "

RRECOMMENDS:packagegroup-base-ipsec = "\
    ${KERNEL_PACKAGE_NAME}-module-ipsec"

#
# packagegroup-base-wifi contain everything needed to get WiFi working
# WEP/WPA connection needs to be supported out-of-box
#
# Choose either 'wpa-supplicant' or 'iwd' as wireless-daemon
WIRELESS_DAEMON ??= "wpa-supplicant"
SUMMARY:packagegroup-base-wifi = "WiFi support"
RDEPENDS:packagegroup-base-wifi = "\
    iw \
    wireless-regdb-static \
    ${WIRELESS_DAEMON} \
"

RRECOMMENDS:packagegroup-base-wifi = "\
    ${@bb.utils.contains('MACHINE_FEATURES', 'usbhost', '${KERNEL_PACKAGE_NAME}-module-zd1211rw', '',d)} \
    ${KERNEL_PACKAGE_NAME}-module-ieee80211-crypt \
    ${KERNEL_PACKAGE_NAME}-module-ieee80211-crypt-ccmp \
    ${KERNEL_PACKAGE_NAME}-module-ieee80211-crypt-tkip \
    ${KERNEL_PACKAGE_NAME}-module-ieee80211-crypt-wep \
    ${KERNEL_PACKAGE_NAME}-module-ecb \
    ${KERNEL_PACKAGE_NAME}-module-arc4 \
    ${KERNEL_PACKAGE_NAME}-module-crypto_algapi \
    ${KERNEL_PACKAGE_NAME}-module-cryptomgr \
    ${KERNEL_PACKAGE_NAME}-module-michael-mic \
    ${KERNEL_PACKAGE_NAME}-module-aes-generic \
    ${KERNEL_PACKAGE_NAME}-module-aes"

SUMMARY:packagegroup-base-nfc = "Near Field Communication support"
RDEPENDS:packagegroup-base-nfc = "\
    neard"

RRECOMMENDS:packagegroup-base-nfc = "\
    ${KERNEL_PACKAGE_NAME}-module-nfc"

SUMMARY:packagegroup-base-3g = "Cellular data support"
RDEPENDS:packagegroup-base-3g = "\
    ofono"

RRECOMMENDS:packagegroup-base-3g = "\
    ${KERNEL_PACKAGE_NAME}-module-cdc-acm \
    ${KERNEL_PACKAGE_NAME}-module-cdc-wdm"

SUMMARY:packagegroup-base-smbfs = "SMB network filesystem support"
RRECOMMENDS:packagegroup-base-smbfs = "\
    ${KERNEL_PACKAGE_NAME}-module-cifs \
    ${KERNEL_PACKAGE_NAME}-module-smbfs"

SUMMARY:packagegroup-base-cramfs = "cramfs filesystem support"
RRECOMMENDS:packagegroup-base-cramfs = "\
    ${KERNEL_PACKAGE_NAME}-module-cramfs"

#
# packagegroup-base-nfs provides ONLY client support - server is in nfs-utils package
#
SUMMARY:packagegroup-base-nfs = "NFS network filesystem support"
RDEPENDS:packagegroup-base-nfs = "\
    rpcbind"

RRECOMMENDS:packagegroup-base-nfs = "\
    ${KERNEL_PACKAGE_NAME}-module-nfs "

SUMMARY:packagegroup-base-zeroconf = "Zeroconf support"
RDEPENDS:packagegroup-base-zeroconf = "\
    avahi-daemon"
RDEPENDS:packagegroup-base-zeroconf:append:libc-glibc = "\
    libnss-mdns \
    "

SUMMARY:packagegroup-base-ipv6 = "IPv6 support"
RDEPENDS:packagegroup-base-ipv6 = "\
    "

RRECOMMENDS:packagegroup-base-ipv6 = "\
    ${KERNEL_PACKAGE_NAME}-module-ipv6 "

SUMMARY:packagegroup-base-serial = "Serial port support"
RDEPENDS:packagegroup-base-serial = "\
    setserial \
    lrzsz "

SUMMARY:packagegroup-base-phone = "Cellular telephony (voice) support"
RDEPENDS:packagegroup-base-phone = "\
    ofono"
