#!/bin/sh

echo "Installing librarian-puppet"
gem install librarian-puppet --no-rdoc --no-ri

echo "Running librarian-puppet install"
mkdir /etc/puppet
cd /etc/puppet
cp /vagrant/Puppetfile .
librarian-puppet install

echo 'Copying vagrant-install-files...'
mkdir /install
cp -rn /vagrant-install-files/* /install

echo "puppet-boostrap.sh done"