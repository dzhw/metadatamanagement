# -*- coding: utf-8 -*-
from robot.libraries.BuiltIn import BuiltIn
from six.moves.http_client import HTTPConnection

import base64
import os
import re


try:
    import json
    json  # pyflakes
except ImportError:
    import simplejson as json


USERNAME_ACCESS_KEY = re.compile('^(http|https):\/\/([^:]+):([^@]+)@')


class SauceLabs:

    def report_test_status(self, name, status, tags=[], remote_url=''):
        """Report test status and tags to SauceLabs
        """
        job_id = BuiltIn().get_library_instance(
            'ExtendedSelenium2Library')._current_browser().session_id

        if USERNAME_ACCESS_KEY.match(remote_url):
            username, access_key =\
                USERNAME_ACCESS_KEY.findall(remote_url)[0][1:]
        else:
            username = os.environ.get('SAUCE_USERNAME')
            access_key = os.environ.get('SAUCE_ACCESS_KEY')

        if not job_id:
            return u"No Sauce job id found. Skipping..."
        elif not username or not access_key:
            return u"No Sauce environment variables found. Skipping..."

        token = base64.encodestring('%s:%s' % (username, access_key))[:-1]
        body = json.dumps({'name': name,
                           'passed': status == 'PASS',
                           'tags': tags})

        connection = HTTPConnection('saucelabs.com')
        connection.request('PUT', '/rest/v1/%s/jobs/%s' % (
            username, job_id), body,
            headers={'Authorization': 'Basic %s' % token}
        )
        return connection.getresponse().status
