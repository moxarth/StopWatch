Enterprise Search | Microsoft Teams Connector
===================================================

This connector synchronizes and enables searching the data of Microsoft Teams.

Requirements
------------

This connector requires:

* Python >= 3.6
* Workplace Search >= 7.13.0 and a Platinum+ license.
* Microsoft Developer Account for logging in to Microsoft Teams and Microsoft Azure.
* Java 7 or above

Installation
------------
This connector is a python package that can be installed as a package locally ::

    make install_package

For the current user, this command will install the pre-requisites required for the package and will then install the whole package for the user
After the package is installed, you can open a new shell and run the connector itself ::

    ees_microsoftteams <cmd>

<cmd> is the connector command, which includes:

- **bootstrap** : To create a content source in Enterprise Search
- **full-sync** : To synchronize all data from Microsoft Teams to Enterprise Search
- **incremental-sync** : To synchronize recent data from Microsoft Teams to Enterprise Search
- **deletion-sync** : To remove from Enterprise Search the data recently deleted from Microsoft Teams

The connector will install supplied ms_teams_connector.yml file into the package data files and use it when ran without -c option.
You can run the connector with supplied ms_teams_connector.yml file without adding the -c option, provided you edit supplied ms_teams_connector.yml
file **after** installing the package, or run connector with -c <FILE_NAME> pointing to the config file you're willing to use, for example ::

    ees_microsoftteams -c ms_teams_connector.yml full-sync

By default the connector will put its default config file into a `config` directory along the executable. To find the config file
you can run `which ees_microsoftteams` to see where the executable of the connector is, then run `cd ../config` and you'll find yourself
in the directory with a default `ms_teams_connector.yml` file.

Configuration file
------------------

Required fields in the configuration file:

* username
* password
* application_id
* client_secret
* tenant_id
* enterprise_search.access_token
* enterprise_search.source_id
* enterprise_search.host_url

The remaining parameters are optional and have a default value.

Bootstrapping
-------------

Before indexing can begin, you need a new content source to index against. You
can either get it by creating a new `custom API source <https://www.elastic.co/guide/en/workplace-search/current/workplace-search-custom-api-sources.html>`_
from Workplace Search admin dashboard or you can just bootstrap it using the
bootstrap.py file. To use bootstrap.py, make sure you have specified
'enterprise_search.host_url' and 'workplace_search.api_key' in the
ms_teams_connector.yml file. Run the bootstrap command ::

    ees_microsoftteams -c ms_teams_connector.yml bootstrap --name <Name of the Content Source> --user <Admin Username>

Here, the parameter 'name' is _required_ while 'user' is _optional_.
You will be prompted to share the user's password if 'user' parameter was specified above. If the parameter 'user' was not specified, the connector would use 'workplace_search.api_key' specified in the configuration file for bootstrapping the content source.

Once the content source is created, the content source ID will be printed on the terminal. You can now move on to modifying the configuration file.

Testing connectivity
====================

You can check the connectivity with Microsoft Teams and Workplace Search server using following command :: 
    
    make test_connectivity

This command will attempt to do:

* check connectivity with Workplace Search
* check connectivity with Microsoft Teams
* test the basic ingestion and deletion to the Workplace Search

Running the Connector
---------------------

Running a specific functionality as a daemon process
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

It's possible to run the connectors as a cron job. A sample crontab file is provided in `cron/connector.example` file.
You can edit and then add it manually to your crontab with `crontab -e` or if your system supports cron.d copy or symlink it into /etc/cron.d/ directory.

The connector will emit logs into stdout and stderr, if logs are needed consider simply piping the output of connectors into
desired file, for example the crontab if you've put config file into `~/.config/ms_teams_connector.yml` and
want to have logs in `~/` can look like::

    0 */2 * * * ees_microsoftteams incremental-sync >> ~/incremental-sync.log
    0 0 */2 * * ees_microsoftteams full-sync >> ~/full-sync.log
    0 * * * * ees_microsoftteams deletion-sync >> ~/deletion-sync.log

Indexing
========

You are all set to begin synchronizing document to Workplace Search. Run the `incremental-sync` command to start the synchronization. Each consecutive run of `incremental-sync` will restart from the same place where the previous run ended.
If the permission fetching is enabled in the configuration file, incremental sync also handles the permission fetching from the Microsoft Teams and ingests the documents with the permissions. This would replicate the permissions from Microsoft Teams to Workplace Search.
Run the following command for incremental-sync ::

    ees_microsoftteams -c ms_teams_connector.yml incremental-sync

Full sync ensures indexing occurs from the _start_time_ provided in the configuration file till the current time of execution. To run full sync, execute the `full-sync` command.

Note: The default max limit configured in enterprise search settings for each document indexed is 102400 bytes. This can be changed from the enterprise search yml configuration file.

The connector inherently uses Tika module for parsing file contents from attachments. `Tika-python <https://github.com/chrismattmann/tika-python>`_ uses Apache Tika REST server. To use this library, you need to have Java 7+ installed on your system as tika-python starts up the Tika REST server in the background.
Tika Server also detects contents from images by automatically calling Tesseract OCR. To allow Tika to also extract content from images, you need to make sure tesseract is on your path and then restart tika-server in the backgroud(if it is already running), by doing **ps aux | grep tika | grep server** and then **kill -9 <pid>**

Removing files deleted in Microsoft Teams from Enterprise Search
================================================================

When items are deleted from Microsoft Teams, a separate process is required to update Workplace Search accordingly. Run the `deletion-sync` command for deleting the records from Workplace Search.
Run the following command for deletion-sync ::

    ees_microsoftteams -c ms_teams_connector.yml deletion-sync
