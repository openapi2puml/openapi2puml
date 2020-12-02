FROM gitpod/workspace-full

RUN sudo apt-get update -q \
   && sudo apt-get install graphviz -yq \
   && sudo rm -rf /var/lib/apt/lists/*