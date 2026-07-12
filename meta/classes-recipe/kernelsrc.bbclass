#
# Copyright OpenEmbedded Contributors
#
# SPDX-License-Identifier: MIT
#

S = "${STAGING_KERNEL_DIR}"
deltask do_fetch
deltask do_unpack
do_patch[depends] += "virtual/kernel:do_shared_workdir"
do_patch[noexec] = "1"
do_package[depends] += "virtual/kernel:do_populate_sysroot"

inherit linux-kernel-base

KERNEL_VERSION = "${@get_kernelversion_file("${STAGING_KERNEL_BUILDDIR}", "${KERNEL_PACKAGE_NAME}")}"
LOCAL_VERSION = "${@get_kernellocalversion_file("${STAGING_KERNEL_BUILDDIR}", "${KERNEL_PACKAGE_NAME}")}"

# The final packages get the kernel version instead of the default 1.0
python do_package:prepend() {
    kernel_version = oe.utils.read_file('%s/%s-abiversion' % (d.getVar("STAGING_KERNEL_BUILDDIR"), d.getVar("KERNEL_PACKAGE_NAME")))
    d.setVar('KERNEL_VERSION', kernel_version)
    d.setVar('PKGV', d.getVar("KERNEL_VERSION").split("-")[0])
}
