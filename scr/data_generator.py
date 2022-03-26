### Generate fake names, addresses, IDs, titles, etc. Save in a text file
from faker import Faker
from collections import OrderedDict
from datetime import timedelta


fake = Faker('en_US')
f_len = 500

# full names
with open('fake_data/full_name.txt', 'w+') as f:
    for i in range(f_len):
        f.write(fake.name() + '\n')
        #print(full_name)

# first names
with open('fake_data/first_name.txt', 'w+') as f:
    for i in range(f_len):
        f.write(fake.first_name() + '\n')

# last names
with open('fake_data/last_name.txt', 'w+') as f:
    for i in range(f_len):
        f.write(fake.last_name() + '\n')

# dates
with open('fake_data/dates.txt', 'w+') as f:
    for i in range(f_len):
        f.write(fake.date() + '\n')

# Distributor companies
with open('fake_data/distr_names.txt', 'w+') as f:
    for i in range(f_len):
        f.write(fake.company() + '\n')

# isbn13
isbns = set(fake.unique.isbn13() for i in range(f_len))
if len(isbns) == f_len:
    with open('fake_data/isbn.txt', 'w+') as f:
        for i in range(f_len):
            f.write(list(isbns)[i] + '\n')
else: print('File too short')

# article texts
with open('fake_data/article_text.txt', 'w+') as f:
    for i in range(f_len):
        f.write(fake.file_path(depth=2, category='text') + '\n')

# Distributor IDs
with open('fake_data/distr_account_num.txt', 'w+') as f:
    for i in range(f_len):
        f.write(str(i+1) + '\n')

# Distributor type  
with open('fake_data/dist_type.txt', 'w+') as f:
    for i in range(f_len):
        # pick random string with weights
        t_dict = [("Library", 0.45), ("Store", 0.45), ("Other", 0.1)]
        p_type = fake.random_choices(elements=OrderedDict(t_dict))
        f.write(p_type[0] + '\n')

# Distributor address - city
with open('fake_data/distr_city.txt', 'w+') as f:
    for i in range(f_len):
        f.write(fake.city() + '\n')

# Distributor address - street
with open('fake_data/distr_street.txt', 'w+') as f:
    for i in range(f_len):
        f.write(fake.street_address() + '\n')

# Distributor address - phone
with open('fake_data/distr_phone.txt', 'w+') as f:
    for i in range(f_len):
        f.write(fake.phone_number() + '\n')

# Distributor balance
with open('fake_data/distr_balance.txt', 'w+') as f:
    for i in range(f_len):
        f.write(str(fake.pyfloat(right_digits=2,min_value=1,max_value=1000)) + '\n')


# Publication ID
with open('fake_data/publication_ID.txt', 'w+') as f:
    for i in range(f_len):
        f.write(str(i+1) + '\n')

# publication titles 
with open('fake_data/pub_title.txt', 'w+') as f:
    for i in range(f_len):
        # generate random int with weights
        n_w = fake.random_element(elements=OrderedDict([(10, 0.65), (15, .25), (20, 0.1)]))
        #generate text of a given character length
        f.write(fake.text(n_w)[:-1] + '\n')

# publication type  
with open('fake_data/pub_type.txt', 'w+') as f:
    for i in range(f_len):
        # pick random string with weights
        t_dict = [("Book", 0.35), ("Novel", 0.35), ("Magazine", 0.15), ("Journal", 0.15)]
        p_type = fake.random_choices(elements=OrderedDict(t_dict))
        f.write(p_type[0] + '\n')

# publication topic 
with open('fake_data/pub_topic.txt', 'w+') as f:
    for i in range(f_len):
        # pick random string with weights
        t_dict = [("Celebrity news", 0.25), ("Adventure", 0.25), ("Science", 0.25), ("Databases", 0.25)]
        p_type = fake.random_choices(elements=OrderedDict(t_dict))
        f.write(p_type[0] + '\n')

# issuetitle for magazines
with open('fake_data/issue_title_mag.txt', 'w+') as f:
    for i in range(f_len):
        r_date = fake.day_of_month()
        r_month = fake.month_name()          
        f.write(r_month + '-' + r_date + '\n')

# issuetitle for journals
with open('fake_data/issue_title_journal.txt', 'w+') as f:
    for i in range(f_len):
        r_volume = str(fake.random_int(min=1, max=999))
        r_issue = str(fake.random_int(min=1, max=999))
        v_str = 'Volume ' + r_volume + ' Issue ' + r_issue
        f.write(v_str + '\n')

# article titles 
with open('fake_data/article_title.txt', 'w+') as f:
    for i in range(f_len):
        # generate random int with weights
        n_w = fake.random_element(elements=OrderedDict([(10, 0.65), (15, .25), (20, 0.1)]))
        #generate text of a given character length
        f.write(fake.text(n_w)[:-1] + '\n')

# article topic 
with open('fake_data/article_topic.txt', 'w+') as f:
    for i in range(f_len):
        # pick random string with weights
        t_dict = [("Celebrity news", 0.25), ("Stories", 0.25), ("Genetics", 0.25), ("Coronavirus", 0.25)]
        p_type = fake.random_choices(elements=OrderedDict(t_dict))
        f.write(p_type[0] + '\n')

# edition number
with open('fake_data/edition_number.txt', 'w+') as f:
    for i in range(f_len):      
        f.write(str(fake.random_int(min=1, max=10)) + '\n')

# chapter titles 
with open('fake_data/chapter_title.txt', 'w+') as f:
    for i in range(f_len):
        # generate random int with weights
        n_w = fake.random_element(elements=OrderedDict([(10, 0.65), (15, .25), (20, 0.1)]))
        #generate text of a given character length
        f.write(fake.text(n_w)[:-1] + '\n')

# Employee ID
with open('fake_data/employee_ID.txt', 'w+') as f:
    for i in range(f_len):
        f.write(str(i+1) + '\n')

# employee type 
with open('fake_data/employee_type.txt', 'w+') as f:
    for i in range(f_len):
        # pick random string with weights
        t_dict = [("Staff", 0.5), ("Invited", 0.5)]
        p_type = fake.random_choices(elements=OrderedDict(t_dict))
        f.write(p_type[0] + '\n')

# employee status
with open('fake_data/employee_status.txt', 'w+') as f:
    for i in range(f_len):
        # pick random string with weights
        t_dict = [("1", 0.9), ("0", 0.1)]
        p_type = fake.random_choices(elements=OrderedDict(t_dict))
        f.write(p_type[0] + '\n')

# check number
with open('fake_data/check_number.txt', 'w+') as f:
    for i in range(f_len):
        f.write(str(fake.random_number(digits=5, fix_len=True)) + '\n')

# check amount
with open('fake_data/check_amount.txt', 'w+') as f:
    for i in range(f_len):
        f.write(str(fake.pyfloat(right_digits=2,min_value=100,max_value=1000)) + '\n')

# check generation and claim dates
# claim dates are later than generation dates
with open('fake_data/check_claim_dates.txt', 'w+') as f:
    with open('fake_data/check_submit_dates.txt', 'w+') as f1:
        for i in range(f_len):
            ch_date = fake.date_between('-1y','now')
            f1.write(str(ch_date) + '\n')
            # add 1 to 30 days to the submit date
            claim_date = ch_date + timedelta(days=fake.random_int(min=1, max=30))
            t_dict = [(str(claim_date), 0.9), ("NULL", 0.1)]
            p_type = fake.random_choices(elements=OrderedDict(t_dict))
            f.write(p_type[0] + '\n')

# Order ID
with open('fake_data/order_ID.txt', 'w+') as f:
    for i in range(f_len):
        f.write(str(i+1) + '\n')

# Order - number of copies
with open('fake_data/num_copies.txt', 'w+') as f:
    for i in range(f_len):
        f.write(str(fake.random_int(min=1, max=100)) + '\n')

# Orders - produced by date
with open('fake_data/produceby_dates.txt', 'w+') as f1:
    for i in range(f_len):
        ch_date = fake.date_between('-1y','+1y')
        f1.write(str(ch_date) + '\n')
