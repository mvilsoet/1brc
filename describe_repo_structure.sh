#!/bin/bash

# Enable globbing for hidden files and include hidden files and directories
shopt -s nullglob dotglob

function traverse() {
    local dir="$1"
    local prefix="$2"

    # Get all entries in the directory
    local entries=("$dir"/*)
    local total=${#entries[@]}

    # If more than 10 entries, print "10+ files..." and return
    if [ $total -gt 15 ]; then
        echo "${prefix}10+ files..."
        return
    fi

    local index=0

    for entry in "${entries[@]}"; do
        ((index++))
        local base=$(basename "$entry")

        # Determine connector and prefix for tree structure
        if [ $index -eq $total ]; then
            local connector="|__ "
            local new_prefix="$prefix    "
        else
            local connector="|-- "
            local new_prefix="$prefix|   "
        fi

        if [ -d "$entry" ]; then
            echo "${prefix}${connector}${base}/"
            traverse "$entry" "$new_prefix"
        else
            echo "${prefix}${connector}${base}"
        fi
    done
}

# Start traversal from the current directory
echo "."
traverse "." ""
