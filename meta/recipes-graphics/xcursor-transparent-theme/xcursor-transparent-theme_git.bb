SUMMARY = "Transparent X11 cursor theme for touchscreens"
HOMEPAGE = "http://www.matchbox-project.org/"
BUGTRACKER = "http://bugzilla.yoctoproject.org/"

LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f"

SECTION = "x11/base"

SRCREV = "23c8af5ba4a1b7efbaf0bbca59a65ff7e10a1a06"
PV = "0.1.1+git"

SRC_URI = "git://git.yoctoproject.org/${BPN};branch=master;protocol=https"
UPSTREAM_CHECK_COMMITS = "1"

inherit autotools allarch

FILES:${PN} = "${datadir}/icons/xcursor-transparent/cursors/*"
