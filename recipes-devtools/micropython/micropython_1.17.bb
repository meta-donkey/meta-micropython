# -*- sh -*-
DESCRIPTION = "\
    MicroPython is a lean and fast implementation of the Python 3 programming \
    language \
"
HOMEPAGE = "https://micropython.org"
SECTION = "devel/python"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=dc72ac7285b72e4d269fd6bc5ce38095"

inherit autotools-brokensep

INC_PR = "r1"
PR = "${INC_PR}.0"

SRC_URI = " \
	gitsm://github.com/micropython/micropython.git;name=src;tag=v${PV} \
"

S = "${WORKDIR}/git"

DEPENDS = "libffi"

# the compilation is producing warnings
# CPPFLAGS:append = " -Wno-error"

# switching MICROPY_USE_READLINE on will generate warnings
EXTRA_OEMAKE = " \
    -C ${S}/ports/unix \
    MICROPY_USE_READLINE=0 \
    V=1 \
    DESTDIR="${D}" \
    CC="${CC}" \
    LD="${LD}" \
    CROSS_COMPILE="${TARGET_PREFIX}" \
    PREFIX="${D}/usr" \
    STRIP="true" \
"

do_compile() {
    # mpy-cross must be build using host cc
    make -j ${BB_NUMBER_THREADS} -C ${S}/mpy-cross
#    oe_runmake axtls
    oe_runmake micropython
}

do_configure() {
    :
}

do_install() {
    install -d ${D}/usr/bin
    install -m 0755 ${S}/ports/unix/micropython ${D}/usr/bin
}

RRECOMMENDS_${PN} = "micropython-lib"

INSANE_SKIP_${PN} = "already-stripped"

BBCLASSEXTEND = "native"
