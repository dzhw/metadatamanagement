# -*- coding: utf-8 -*-
from robot.libraries.BuiltIn import BuiltIn
from six.moves.http_client import HTTPSConnection

import base64
import os
import re


try:
    import json
    json  # pyflakes
except ImportError:
    import simplejson as json


USERNAME_ACCESS_KEY = re.compile('^(http|https):\/\/([^:]+):([^@]+)@')


class Browserstack:

    def report_test_status(self, name, status, tags=[], remote_url=''):
        """Report test status and tags to Browserstack
        """
        job_id = BuiltIn().get_library_instance(
            'ExtendedSelenium2Library')._current_browser().session_id

        if USERNAME_ACCESS_KEY.match(remote_url):
            username, access_key =\
                USERNAME_ACCESS_KEY.findall(remote_url)[0][1:]
        else:
            username = os.environ.get('BROWSERSTACK_USERNAME')
            access_key = os.environ.get('BROWSERSTACK_ACCESS_KEY')

        if not job_id:
            return u"No Sauce job id found. Skipping..."
        elif not username or not access_key:
            return u"No Sauce environment variables found. Skipping..."

        token = base64.encodestring('%s:%s' % (username, access_key))[:-1]
        body = json.dumps({ 'status': 'passed' if status == 'PASS' else 'failed',
                           'reason': 'All robot tests passed!' if status == 'PASS' else 'Some robot tests failed!'})

        connection = HTTPSConnection('api.browserstack.com')
        connection.request('PUT', '/automate/sessions/%s.json' % job_id, body,
            headers={'Authorization': 'Basic %s' % token,
                     'Content-Type': 'application/json'}
        )
        return connection.getresponse().status
