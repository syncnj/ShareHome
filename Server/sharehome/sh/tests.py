from django.test import TestCase
import datetime

from django.utils import timezone
from django.test import TestCase

from django.contrib.auth.models import User


class UserMethodTests(TestCase):

    def User_name_chaged(self):
        """
        was_published_recently() should return False for questions whose
        pub_date is in the future.
        """
        u=User.objects.get(username='test')
        u.last_name='new'
        u.save()
        self.assertIs(u.last_name,'new'),
# Create your tests here.
