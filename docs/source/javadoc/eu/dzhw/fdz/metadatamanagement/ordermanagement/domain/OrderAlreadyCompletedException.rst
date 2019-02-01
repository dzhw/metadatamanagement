OrderAlreadyCompletedException
==============================

.. java:package:: eu.dzhw.fdz.metadatamanagement.ordermanagement.domain
   :noindex:

.. java:type:: public class OrderAlreadyCompletedException extends IllegalArgumentException

   Orders with \ :java:ref:`OrderState.ORDERED`\  must not be updated. This exception should be thrown whenever an update attempt is made on such orders.

