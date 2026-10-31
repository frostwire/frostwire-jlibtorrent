#!/usr/bin/perl
# Resolves the placeholders includes/libtorrent_ownership.i leaves in the generated proxies
# (frostwire/frostwire-jlibtorrent#337). run-swig.sh runs it on every generated .java file:
#
#   JLIBTORRENT_BORROW_OWNER -> `this` inside an instance method, `null` inside a static one
#   JLIBTORRENT_CTOR_BORROW  -> the first proxy argument of the constructor call on that line,
#                               the `x` of `T.getCPtr(x), x`
#
# Java typemaps can't tell static methods from instance methods, so this walks the braces: each
# `{` opens a class body (class/interface/enum, or an anonymous class), a member (a method opened
# directly in a class body; static if its declaration says so) or a block inside a member. The
# placeholder takes the context of the innermost member. It fails on anything it can't resolve.
use strict;
use warnings;

my $status = 0;
for my $file (@ARGV) {
    open(my $in, '<', $file) or die "$file: $!";
    my @lines = <$in>;
    close($in);

    my @stack;    # 'class', 'static', 'instance' or 'block'
    my $changed = 0;
    for my $n (0 .. $#lines) {
        my $line = $lines[$n];
        if ($line =~ /JLIBTORRENT_BORROW_OWNER|JLIBTORRENT_CTOR_BORROW/) {
            my $context;
            for my $kind (reverse @stack) {
                next if $kind eq 'block';
                $context = $kind;
                last;
            }
            if (!defined $context || $context eq 'class') {
                # a field initializer directly in a class body
                $context = $line =~ /\bstatic\b/ ? 'static' : 'instance';
            }
            my $owner = $context eq 'static' ? 'null' : 'this';
            $changed += $line =~ s/\bJLIBTORRENT_BORROW_OWNER\b/$owner/g;
            if ($line =~ /\bJLIBTORRENT_CTOR_BORROW\b/) {
                if ($line =~ /\.getCPtr\((\w+)\), \1\b/) {
                    my $arg = $1;
                    $changed += $line =~ s/\bJLIBTORRENT_CTOR_BORROW\b/$arg/g;
                } else {
                    print STDERR "$file:" . ($n + 1) . ": JLIBTORRENT_CTOR_BORROW without a proxy argument\n";
                    $status = 1;
                }
            }
            $lines[$n] = $line;
        }

        # Track the braces this line opens and closes, ignoring string and char literals.
        (my $code = $line) =~ s/"(?:[^"\\]|\\.)*"|'(?:[^'\\]|\\.)*'//g;
        $code =~ s{//.*$}{};
        # SWIG writes each declaration on one line: what opens a brace is on the same line.
        my $prefix = '';
        for my $ch (split //, $code) {
            if ($ch eq ';') {
                $prefix = '';
            } elsif ($ch eq '{') {
                my $enclosing = @stack ? $stack[-1] : undef;
                my $kind;
                if ($prefix =~ /\b(class|interface|enum)\b/ || $prefix =~ /\bnew\s+[\w.<>, ?]+\([^()]*\)\s*$/) {
                    $kind = 'class';
                } elsif (defined $enclosing && $enclosing eq 'class') {
                    $kind = $prefix =~ /\bstatic\b/ ? 'static' : 'instance';
                } else {
                    $kind = 'block';
                }
                push @stack, $kind;
                $prefix = '';
            } elsif ($ch eq '}') {
                pop @stack;
                $prefix = '';
            } else {
                $prefix .= $ch;
            }
        }
    }

    for my $n (0 .. $#lines) {
        if ($lines[$n] =~ /JLIBTORRENT_(BORROW_OWNER|CTOR_BORROW)/) {
            print STDERR "$file:" . ($n + 1) . ": unresolved placeholder\n";
            $status = 1;
        }
    }
    if ($changed) {
        open(my $out, '>', $file) or die "$file: $!";
        print $out @lines;
        close($out);
    }
}
exit $status;
