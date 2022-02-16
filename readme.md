* Python
** pip install pyyaml

* ruby
** install rbenv
** install ruby 3.x
** gem install algorithms

* js
$ NPM_PACKAGES="$HOME/.npm-packages"
$ mkdir -p "$NPM_PACKAGES"
$ echo "prefix = $NPM_PACKAGES" >> ~/.npmrc
# NPM packages in homedir
NPM_PACKAGES="$HOME/.npm-packages"

# Tell our environment about user-installed node tools
PATH="$NPM_PACKAGES/bin:$PATH"
# Unset manpath so we can inherit from /etc/manpath via the `manpath` command
unset MANPATH  # delete if you already modified MANPATH elsewhere in your configuration
MANPATH="$NPM_PACKAGES/share/man:$(manpath)"

# Tell Node about these packages
NODE_PATH="$NPM_PACKAGES/lib/node_modules:$NODE_PATH"

Now when you do an npm install -g, NPM will install the libraries into ~/.npm-packages/lib/node_modules, and link executable tools into ~/.npm-packages/bin, which is in your PATH

source - https://stackoverflow.com/questions/10081293/install-npm-into-home-directory-with-distribution-nodejs-package-ubuntu

** npm install

* perl
** Sub::Identify, JSON

