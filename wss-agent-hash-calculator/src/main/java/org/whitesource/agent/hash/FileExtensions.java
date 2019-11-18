package org.whitesource.agent.hash;

import java.util.*;

/**
 * Created by anna.rozin
 */
public class FileExtensions {

    /* --- Static members --- */

    private static final String FILE_EXTENSION_PREFIX_REGEX = ".*\\.";

    private static final String OR_REGEX = "|";

    public static final String GLOB_PATTERN_PREFIX = "**/*.";

    public static final String BINARY_FILE_EXTENSION_REGEX;

    public static final List<String> SOURCE_EXTENSIONS = Arrays.asList("4th", "6pl", "6pm", "8xk", "8xk.txt", "8xp",
            "8xp.txt", "E", "ML", "_coffee", "_js", "_ls", "abap", "ada", "adb", "ado", "adp", "ads", "agda", "ahk",
            "ahkl", "aidl", "aj", "al", "als", "ampl", "apl", "app.src", "applescript", "arc", "as", "asax", "asc",
            "ascx", "asd", "ash", "ashx", "asmx", "asp", "aspx", "au3", "aug", "auk", "aw", "awk", "axd", "axi",
            "axi.erb", "axs", "axs.erb", "b", "bas", "bash", "bat", "bats", "bb", "befunge", "bf", "bison", "bmx",
            "bones", "boo", "boot", "brd", "bro", "brs", "bsl", "bsv", "builder", "bzl", "c", "c++", "cake", "capnp",
            "cats", "cbl", "cc", "ccp", "cdf", "ceylon", "cfc", "cfm", "cfml", "cgi", "ch", "chpl", "chs", "cirru",
            "cjsx", "ck", "cl", "cl2", "click", "clj", "cljc", "cljs", "cljs.hl", "cljscm", "cljx", "clp", "cls",
            "clw", "cmd", "cob", "cobol", "coffee", "com", "command", "coq", "cp", "cpp", "cps", "cpy", "cr", "cs",
            "csd", "cshtml", "csx", "ctp", "cu", "cuh", "cw", "cxx", "cy", "d", "dart", "dats", "db2", "dcl", "decls",
            "dfm", "di", "djs", "dlm", "dm", "do", "doh", "dpr", "druby", "duby", "dyalog", "dyl", "dylan", "e", "ec",
            "ecl", "eclxml", "eh", "el", "eliom", "eliomi", "elm", "em", "emacs", "emacs.desktop", "emberscript", "eq",
            "erl", "es", "es6", "escript", "ex", "exs", "eye", "f", "f03", "f08", "f77", "f90", "f95", "factor", "fan",
            "fancypack", "fcgi", "feature", "flex", "flux", "for", "forth", "fp", "fpp", "fr", "frag", "frg", "frm",
            "frt", "frx", "fs", "fsh", "fshader", "fsi", "fsx", "fth", "ftl", "fun", "fx", "fxh", "fy", "g", "g4",
            "gap", "gawk", "gd", "gdb", "gdbinit", "gemspec", "geo", "geom", "gf", "gi", "glf", "glsl", "glslv", "gml",
            "gms", "gnu", "gnuplot", "go", "god", "golo", "gp", "grace", "groovy", "grt", "gs", "gshader", "gsp", "gst",
            "gsx", "gtpl", "gvy", "gyp", "gypi", "h", "h++", "hats", "hb", "hcl", "hh", "hic", "hlean", "hlsl", "hlsli",
            "hpp", "hqf", "hrl", "hs", "hsc", "hx", "hxsl", "hxx", "hy", "i7x", "iced", "icl", "idc", "idr", "ihlp",
            "ijs", "ik", "ily", "inc", "inl", "ino", "intr", "io", "ipf", "ipp", "irbrc", "iss", "j", "jake", "java",
            "jbuilder", "jflex", "ji", "jison", "jisonlex", "jl", "jq", "js", "jsb", "jscad", "jsfl", "jsm", "jsp",
            "jss", "jsx", "kicad_pcb", "kid", "krl", "ksh", "kt", "ktm", "kts", "l", "lagda", "las", "lasso", "lasso8",
            "lasso9", "ldml", "lean", "lex", "lfe", "lgt", "lhs", "lid", "lidr", "lisp", "litcoffee", "ll", "lmi",
            "logtalk", "lol", "lookml", "lpr", "ls", "lsl", "lslp", "lsp", "lua", "lvproj", "ly", "m", "m4", "ma",
            "mak", "make", "mako", "mao", "mata", "matah", "mathematica", "matlab", "mawk", "maxhelp", "maxpat",
            "maxproj", "mcr", "metal", "minid", "mir", "mirah", "mk", "mkfile", "ml", "ml4", "mli", "mll", "mly",
            "mm", "mmk", "mms", "mo", "mod", "model.lkml", "monkey", "moo", "moon", "mq4", "mq5", "mqh", "ms",
            "mspec", "mss", "mt", "mu", "muf", "mumps", "mxt", "myt", "n", "nawk", "nb", "nbp", "nc", "ncl", "ni",
            "nim", "nimrod", "nit", "nix", "njs", "nl", "nlogo", "nqp", "nse", "nsh", "nsi", "nu", "numpy", "numpyw",
            "numsc", "nut", "ny", "omgrofl", "ooc", "opa", "opal", "opencl", "orc", "os", "ox", "oxh", "oxo", "oxygene",
            "oz", "p", "p4", "p6", "p6l", "p6m", "pac", "pan", "parrot", "pas", "pascal", "pasm", "pat", "pb", "pbi",
            "pbt", "pck", "pd", "pd_lua", "pde", "perl", "ph", "php", "php3", "php4", "php5", "phps", "phpt", "pig",
            "pike", "pir", "pkb", "pks", "pl", "pl6", "plb", "plot", "pls", "plsql", "plt", "pluginspec", "plx", "pm",
            "pm6", "pmod", "pod", "podsl", "podspec", "pogo", "pony", "pov", "pp", "pprx", "prg", "pri", "pro", "prolog",
            "prw", "ps1", "psc", "psd1", "psgi", "psm1", "purs", "pwn", "pxd", "pxi", "py", "py3", "pyde", "pyp", "pyt",
            "pyw", "pyx", "qbs", "qml", "r", "r2", "r3", "rabl", "rake", "rb", "rbbas", "rbfrm", "rbmnu", "rbres",
            "rbtbar", "rbuild", "rbuistate", "rbw", "rbx", "rbxs", "rd", "re", "reb", "rebol", "red", "reds", "rei",
            "rex", "rexx", "rg", "rkt", "rktd", "rktl", "rl", "rpy", "rs", "rs.in", "rsc", "rsh", "rsx", "ru", "ruby",
            "sage", "sagews", "sas", "sats", "sbt", "sc", "scad", "scala", "scd", "sce", "sch", "sci", "scm", "sco",
            "scpt", "scrbl", "self", "sexp", "sh", "sh-session", "sh.in", "shader", "shen", "sig", "sj", "sjs", "sl",
            "sld", "sls", "sma", "smali", "sml", "smt", "smt2", "sp", "spec", "spin", "sps", "sqf", "sql", "sra", "sru",
            "srw", "ss", "ssjs", "st", "stan", "sthlp", "sv", "svh", "swift", "t", "tac", "tcc", "tcl", "tf", "thor",
            "thrift", "thy", "tla", "tm", "tmux", "tool", "tpl", "tpp", "ts", "tst", "tsx", "tu", "txl", "uc", "udo",
            "uno", "upc", "ur", "urs", "v", "vala", "vapi", "vark", "vb", "vba", "vbhtml", "vbs", "vcl", "veo", "vert",
            "vh", "vhd", "vhdl", "vhf", "vhi", "vho", "vhs", "vht", "vhw", "view.lkml", "vim", "volt", "vrx", "vsh",
            "vshader", "w", "watchr", "webidl", "wisp", "wl", "wlt", "wlua", "wsgi", "x", "x10", "xc", "xi", "xm",
            "xojo_code", "xojo_menu", "xojo_report", "xojo_script", "xojo_toolbar", "xojo_window", "xpl", "xproc",
            "xpy", "xq", "xql", "xqm", "xquery", "xqy", "xrl", "xs", "xsjs", "xsjslib", "xsl", "xslt", "xsp-config",
            "xsp.metadata", "xtend", "y", "yacc", "yap", "yrl", "yy", "zep", "zimpl", "zmpl", "zpl", "zsh");

    private static final Collection<String> GENERIC_RESOURCE_FILE_EXTENSIONS = new ArrayList<>(
            Arrays.asList("a", "aar", "air", "apk", "ar", "bz2", "car", "crate", "deb", "dll", "dmg", "drpm", "ear", "egg", "exe",
                    "gem", "gzip", "jar", "js", "ko", "min.js", "msi", "nupkg", "pkg.tar.xz", "rpm", "sit", "so", "swc", "swf", "tar",
                    "tar.bz2", "tar.gz", "tbz", "tgz", "udeb", "war", "whl", "zip"));


    public static final List<String> ARCHIVE_EXTENSIONS = Arrays.asList("war", "ear", "zip", "whl", "tar.gz", "tgz", "tar", "car", "aar", "jar");
    private static final Collection<String> DOT_NET_FILE_EXTENSIONS = new ArrayList<>(
            Arrays.asList("dll", "exe"));
    public static final String SOURCE_FILE_PATTERN;
    public static final String BINARY_FILE_PATTERN;
    public static final String ARCHIVE_FILE_PATTERN;
    public static final String[] INCLUDES;
    public static final String[] EXCLUDES = new String[]{"**/*sources.jar", "**/*javadoc.jar", "**/tests/**"};
    public static final String[] ARCHIVE_INCLUDES;
    public static final String[] ARCHIVE_EXCLUDES = new String[]{"**/*sources.jar", "**/*javadoc.jar", "**/tests/**"};


    static {
        SOURCE_FILE_PATTERN = buildRegexString(SOURCE_EXTENSIONS);
        BINARY_FILE_PATTERN = buildRegexString(GENERIC_RESOURCE_FILE_EXTENSIONS);
        ARCHIVE_FILE_PATTERN = buildRegexString(ARCHIVE_EXTENSIONS);

        StringBuilder sb = new StringBuilder();
        sb.append(BINARY_FILE_PATTERN);
        sb.append(OR_REGEX);
        sb.append(ARCHIVE_FILE_PATTERN);
        sb.append(OR_REGEX);
        sb.append(buildRegexString(DOT_NET_FILE_EXTENSIONS));
        BINARY_FILE_EXTENSION_REGEX = sb.toString();

        List<String> allExtensions = new ArrayList<>();
        allExtensions.addAll(SOURCE_EXTENSIONS);
        allExtensions.addAll(GENERIC_RESOURCE_FILE_EXTENSIONS);
        INCLUDES = initializeGlobPattern(allExtensions);
        ARCHIVE_INCLUDES = initializeGlobPattern(ARCHIVE_EXTENSIONS);
    }

    /* --- Private methods --- */

    private static String buildRegexString(Collection<String> extensions) {
        StringBuilder sb = new StringBuilder();
        Iterator<String> iterator = extensions.iterator();
        while (iterator.hasNext()) {
            String extension = iterator.next();
            sb.append(FILE_EXTENSION_PREFIX_REGEX);
            sb.append(extension);
            if (iterator.hasNext()) {
                sb.append(OR_REGEX);
            }
        }
        return sb.toString();
    }

    private static String[] initializeGlobPattern(List<String> extensions) {
        String[] globPatterns = new String[extensions.size()];
        for (int i = 0; i < extensions.size(); i++) {
            globPatterns[i] = GLOB_PATTERN_PREFIX + extensions.get(i);
        }
        return globPatterns;
    }

}