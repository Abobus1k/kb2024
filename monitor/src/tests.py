import unittest
from policies import check_operation


class TestPolicies(unittest.TestCase):
    true_keys = ['default']
    true_headers = [
        {'from': 'ai-connector', 'to': 'redirector'},
        {'from': 'ai-connector', 'to': 'jarvis'},
        {'from': 'ai-connector', 'to': 'geo'},
        {'from': 'jarvis', 'to': 'ai-connector'},
        {'from': 'enemy', 'to': 'weapon'},
        {'from': 'geo', 'to': 'monitoring'},
        {'from': 'geo', 'to': 'stabilizer'},
        {'from': 'monitoring', 'to': 'interface'},
        {'from': 'monitoring', 'to': 'monitoring'},
        {'from': 'monitoring', 'to': 'redirector'},
        {'from': 'redirector', 'to': 'ai-connector'},
        {'from': 'redirector', 'to': 'monitoring'},
        {'from': 'redirector', 'to': 'travel'},
        {'from': 'redirector', 'to': 'weapon'},
        {'from': 'stabilizer', 'to': 'stabilizer'},
        {'from': 'travel', 'to': 'geo'},
        {'from': 'weapon', 'to': 'enemy'},
        {'from': 'weapon', 'to': 'weapon'},
    ]

    def test_keys(self):
        # Testing that only keys "default" and "new-device" are acceptable
        fake_keys = ['asd', '123', 'def', None]

        for k in fake_keys:
            self.assertEqual(check_operation(k, self.true_headers), False)

    def test_headers(self):
        # Testing that headers content is checked
        fake_headers = [{'asd': 'asd'}, {}, {'from': 'ai-connector'}, {'to': 'weapon'}]
        for h in fake_headers:
            self.assertEqual(check_operation(self.true_keys[0], h), False)

    def test_policy(self):
        for k in self.true_keys:
            for h in self.true_headers:
                self.assertEqual(check_operation(k, h), True)

        # False expected
        self.assertEqual(check_operation('default', {'from': 'ai-connector', 'to': 'enemy'}), False)
        self.assertEqual(check_operation('default', {'from': 'ai-connector', 'to': 'monitoring'}), False)
        self.assertEqual(check_operation('default', {'from': 'ai-connector', 'to': 'travel'}), False)
        self.assertEqual(check_operation('default', {'from': 'ai-connector', 'to': 'weapon'}), False)
        self.assertEqual(check_operation('default', {'from': 'ai-connector', 'to': 'stabilizer'}), False)
        self.assertEqual(check_operation('default', {'from': 'ai-connector', 'to': 'interface'}), False)
        self.assertEqual(check_operation('default', {'from': 'redirector', 'to': 'jarvis'}), False)
        self.assertEqual(check_operation('default', {'from': 'redirector', 'to': 'geo'}), False)
        self.assertEqual(check_operation('default', {'from': 'redirector', 'to': 'enemy'}), False)
        self.assertEqual(check_operation('default', {'from': 'redirector', 'to': 'stabilizer'}), False)
        self.assertEqual(check_operation('default', {'from': 'redirector', 'to': 'interface'}), False)
        self.assertEqual(check_operation('default', {'from': 'jarvis', 'to': 'redirector'}), False)
        self.assertEqual(check_operation('default', {'from': 'jarvis', 'to': 'geo'}), False)
        self.assertEqual(check_operation('default', {'from': 'jarvis', 'to': 'travel'}), False)
        self.assertEqual(check_operation('default', {'from': 'jarvis', 'to': 'weapon'}), False)
        self.assertEqual(check_operation('default', {'from': 'jarvis', 'to': 'enemy'}), False)
        self.assertEqual(check_operation('default', {'from': 'jarvis', 'to': 'stabilizer'}), False)
        self.assertEqual(check_operation('default', {'from': 'jarvis', 'to': 'interface'}), False)
        self.assertEqual(check_operation('default', {'from': 'geo', 'to': 'ai-connector'}), False)
        self.assertEqual(check_operation('default', {'from': 'geo', 'to': 'redirector'}), False)
        self.assertEqual(check_operation('default', {'from': 'geo', 'to': 'jarvis'}), False)
        self.assertEqual(check_operation('default', {'from': 'geo', 'to': 'travel'}), False)
        self.assertEqual(check_operation('default', {'from': 'geo', 'to': 'weapon'}), False)
        self.assertEqual(check_operation('default', {'from': 'geo', 'to': 'enemy'}), False)
        self.assertEqual(check_operation('default', {'from': 'geo', 'to': 'interface'}), False)
        self.assertEqual(check_operation('default', {'from': 'monitoring', 'to': 'ai-connector'}), False)
        self.assertEqual(check_operation('default', {'from': 'monitoring', 'to': 'jarvis'}), False)
        self.assertEqual(check_operation('default', {'from': 'monitoring', 'to': 'travel'}), False)
        self.assertEqual(check_operation('default', {'from': 'monitoring', 'to': 'geo'}), False)
        self.assertEqual(check_operation('default', {'from': 'monitoring', 'to': 'weapon'}), False)
        self.assertEqual(check_operation('default', {'from': 'monitoring', 'to': 'enemy'}), False)
        self.assertEqual(check_operation('default', {'from': 'monitoring', 'to': 'stabilizer'}), False)


if __name__ == '__main__':
    unittest.main()
